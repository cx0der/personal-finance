package com.moveableapps.pf.loaders;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;
import com.moveableapps.pf.parsers.CsvParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CsvLoader {

    private static final Logger logger = LogManager.getLogger(CsvLoader.class);

    private final String filePath;
    private final int dateField;
    private final String accountName;
    private final int amountField;
    private final int descriptionField;
    private final BookKeeper bookKeeper;

    public CsvLoader(String filePath, int dateField, String accountName, int amountField, BookKeeper bookKeeper, int descriptionField) {
        this.filePath = filePath;
        this.dateField = dateField;
        this.accountName = accountName;
        this.amountField = amountField;
        this.bookKeeper = bookKeeper;
        this.descriptionField = descriptionField;
    }

    public void load(char delimiter, boolean skipFirstLine, String dateFormatPattern, int precision) {
        Optional<Account> mayBeAccount = bookKeeper.getAccountByName(accountName);
        if (mayBeAccount.isEmpty()) {
            logger.error("Unable to find account " + accountName);
            throw new RuntimeException("Unable to find account " + accountName);
        }

        if (mayBeAccount.get().id().isEmpty()) {
            logger.error("Unable to find account " + accountName);
            throw new RuntimeException("Unable to find account " + accountName);
        }

        DateFormat dateFormat;
        try {
            dateFormat = new SimpleDateFormat(dateFormatPattern);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid date format: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        Map<String, AutoMapping> mappings = bookKeeper.getAllAutoMappings();
        long uncategorizedId = findUncategorizedAccount(mappings);
        int multiplier = (int) Math.pow(10, precision);

        Account account = mayBeAccount.get();
        CsvParser parser = new CsvParser(filePath, delimiter);
        int[] fields = {dateField, amountField, descriptionField};
        List<Map<Integer, String>> entries = parser.parse(skipFirstLine, fields);

        for (Map<Integer, String> entry : entries) {
            if (!entry.isEmpty()) {
                Date postDate;
                try {
                    postDate = dateFormat.parse(entry.get(dateField));
                } catch (ParseException e) {
                    logger.error("Unable to parse date: " + entry.get(dateField) + " skipping record");
                    continue;
                }
                Date entryDate = Date.from(java.time.ZonedDateTime.now().toInstant());
                Transaction transaction = new Transaction(postDate, entryDate, entry.get(descriptionField));
                double amount = Double.parseDouble(entry.get(amountField));
                long amountValue = (long) (amount * multiplier);
                Split accountSplit = new Split(account.id().getAsLong(), 'y', entryDate, amountValue, multiplier);
                Split mappingSplit = new Split(getMappingOrDefault(mappings, entry.get(descriptionField), uncategorizedId), 'y', entryDate, amountValue, multiplier);
                bookKeeper.addTransaction(transaction, List.of(accountSplit, mappingSplit));
            }
        }
    }

    private long findUncategorizedAccount(Map<String, AutoMapping> mappings) {
        Optional<AutoMapping> mapping = Optional.ofNullable(mappings.get("Expenses:Uncategorized"));
        if (mapping.isEmpty()) {
            throw new RuntimeException("Unable to find Uncategorized Account");
        }
        return mapping.get().accountId();
    }

    private long getMappingOrDefault(Map<String, AutoMapping> mappings, String description, long defaultValue) {
        Optional<String> mayMapping = mappings.keySet().stream().filter(d -> description.toLowerCase().contains(d.toLowerCase())).findFirst();
        if (mayMapping.isEmpty()) {
            logger.info("no mapping for description: " + description);
        }
        return mayMapping.isEmpty() ? defaultValue : mappings.get(mayMapping.get()).accountId();
    }
}
