package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;
import com.moveableapps.pf.parsers.CsvParser;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LoadCommand implements Command {
    public static final String COMMAND_NAME = "load";

    @Override
    public int execute(CommandArgs commandArgs, BookKeeper bookKeeper, PrintStream out, PrintStream err) throws Exception {
        LoadCommandArgs args = (LoadCommandArgs) commandArgs;
        out.println("Importing from file: " + args.inputFile);
        out.println("For account: " + args.account);
        out.println("using date format: " + args.dateFormat);
        out.println("Precision: " + args.precision + " decimal places");

        long targetAccountId = getAccountIdOrThrow(bookKeeper, args.account);
        DateFormat dateFormat = getDateFormatOrThrow(args.dateFormat);

        List<Map<Integer, String>> entries = getFileEntries(args);

        if (entries.isEmpty()) {
            out.println("No entries to load");
            return 0;
        }

        Map<String, AutoMapping> mappings = bookKeeper.getAllAutoMappings();

        if (mappings.isEmpty()) {
            err.println("Cannot proceed without mappings");
            throw new IllegalStateException("Cannot proceed without mappings");
        }
        Map<Transaction, List<Split>> txnMap = mapEntriesToTransactions(mappings, entries, dateFormat,
                targetAccountId, args, out, err);
        if (args.dryRunOnly) {
            return 0;
        }
        List<Transaction> addedTransactions = saveTransactions(txnMap, bookKeeper);
        out.println("Added " + addedTransactions.size() + " transactions out of " + entries.size() + " entries successfully");
        return 0;
    }

    private List<Map<Integer, String>> getFileEntries(LoadCommandArgs args) {
        int[] fieldsToSelect = {args.dateField, args.amountField, args.descriptionField};
        CsvParser parser = new CsvParser();
        return parser.parse(args.inputFile, args.delimiter, args.skipFirstLine, fieldsToSelect);
    }

    private DateFormat getDateFormatOrThrow(String dateFormat) {
        return new SimpleDateFormat(dateFormat);
    }

    private long getAccountIdOrThrow(BookKeeper bookKeeper, String accountName) {
        Optional<Account> mayBeAccount = bookKeeper.getAccountByName(accountName);
        Account account = mayBeAccount.orElseThrow(
                () -> new IllegalArgumentException("Unable to find account by name: '" + accountName + "'"));

        return account.id().orElseThrow(() -> new IllegalStateException("Account did not have an Id"));
    }

    private Map<Transaction, List<Split>> mapEntriesToTransactions(Map<String, AutoMapping> mappings,
                                                                   List<Map<Integer, String>> entries,
                                                                   DateFormat dateFormat,
                                                                   long targetAccountId,
                                                                   LoadCommandArgs args, PrintStream out, PrintStream err) {
        Map<Transaction, List<Split>> transactionMap = new HashMap<>();
        int multiplier = (int) Math.pow(10, args.precision);
        AtomicInteger entryCount = new AtomicInteger(0);

        entries.forEach(entry -> {
            Date postDate;
            double amount;
            long mappedAccountId;

            // processing try-catch block
            try {
                postDate = dateFormat.parse(entry.get(args.dateField));
                amount = Double.parseDouble(entry.get(args.amountField));
                mappedAccountId = getMappedAccountIdOrThrow(mappings, entry.get(args.descriptionField));
            } catch (ParseException e) {
                String errorMessage = String.format("Unable to parse date: %s", entry.get(args.dateField));
                processErrorAndThrow(errorMessage, err, out, entryCount.incrementAndGet(), args.dryRunOnly);
                return;
            } catch (NumberFormatException e) {
                String errorMessage = String.format("Unable to parse amount: %s", entry.get(args.amountField));
                processErrorAndThrow(errorMessage, err, out, entryCount.incrementAndGet(), args.dryRunOnly);
                return;
            } catch (NoSuchElementException e) {
                err.println(e.getMessage());
                processErrorAndThrow(e.getMessage(), err, out, entryCount.incrementAndGet(), args.dryRunOnly);
                return;
            }

            if (!args.dryRunOnly) {
                Date entryDate = Date.from(ZonedDateTime.now().toInstant());
                Transaction transaction = new Transaction(postDate, entryDate, entry.get(args.descriptionField));
                long amountValue = (long) amount * multiplier;
                List<Split> splits = getSplits(targetAccountId, mappedAccountId, entryDate, amountValue, multiplier);
                out.println("Processed entry: " + entryCount.incrementAndGet());
                transactionMap.put(transaction, splits);
            }
        });
        return transactionMap;
    }

    private long getMappedAccountIdOrThrow(Map<String, AutoMapping> mappings, String description) {
        Optional<String> mayBeKey = mappings.keySet()
                .stream().filter(d -> description.toLowerCase().contains(d.toLowerCase()))
                .findFirst();
        if (mayBeKey.isPresent()) {
            return mappings.get(mayBeKey.get()).accountId();
        }
        throw new NoSuchElementException("Unable to mapping for description: '" + description + "'");
    }

    private List<Split> getSplits(long accountId, long mappedAccountId, Date entryDate, long amountValue, int multiplier) {
        return List.of(
                new Split(accountId, 'y', entryDate, amountValue, multiplier),
                new Split(mappedAccountId, 'y', entryDate, amountValue, multiplier)
        );
    }

    private void processErrorAndThrow(String errorMessage, PrintStream err, PrintStream out, int entryCount, boolean dryRun) {
        err.println(errorMessage);
        if (!dryRun) throw new RuntimeException(errorMessage);
        out.println("Skipping further processing of entry: " + entryCount);
    }

    private List<Transaction> saveTransactions(Map<Transaction, List<Split>> details, BookKeeper bookKeeper) {
        return details.entrySet().stream()
                .map(entry -> bookKeeper.addTransaction(entry.getKey(), entry.getValue()))
                .peek(transaction -> {
                    String msg = String.format("Added Transaction for description: %s", transaction.description());
                    System.out.println(msg);
                })
                .collect(Collectors.toList());
    }
}
