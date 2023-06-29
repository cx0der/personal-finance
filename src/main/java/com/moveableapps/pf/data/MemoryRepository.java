package com.moveableapps.pf.data;

import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MemoryRepository implements Repository {

    private final List<Account> accounts;
    private final List<Transaction> transactions;
    private final List<Split> splits;
    private final List<AutoMapping> autoMappings;

    public MemoryRepository() {
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        splits = new ArrayList<>();
        autoMappings = new ArrayList<>();
    }

    @Override
    public long addAccount(Account account) {
        long newAccountId = this.accounts.size() + 1;
        this.accounts.add(account.copyWithAccountId(newAccountId));
        return newAccountId;
    }

    @Override
    public List<Account> getAllAccounts() {
        return this.accounts;
    }

    @Override
    public Optional<Account> getAccountByName(String name) {
        return this.accounts.stream().filter(account -> account.name().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public long addTransaction(Transaction transaction) {
        long newId = transactions.size() + 1;
        this.transactions.add(transaction.copyWithTransactionId(newId));
        return newId;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return this.transactions;
    }

    @Override
    public int addSplits(long transactionId, List<Split> splits) {
        AtomicLong nextId = new AtomicLong(this.splits.size());
        splits.forEach(s -> this.splits.add(s.copyWithSplitId(nextId.incrementAndGet()).copyWithTxnId(transactionId)));
        return splits.size();
    }

    @Override
    public List<Split> getSplitsForTransaction(long transactionId) {
        return this.splits.stream().filter(split -> split.txnId().isPresent() && split.txnId().getAsLong() == transactionId).toList();
    }

    @Override
    public long addAutoMapping(AutoMapping mapping) {
        long newId = this.autoMappings.size() + 1;
        this.autoMappings.add(mapping.copyWithMappingId(newId));
        return newId;
    }

    @Override
    public Map<String, AutoMapping> getAllAutoMappings() {
        // Found out about Function.identity() from https://www.baeldung.com/java-list-to-map
        return this.autoMappings.stream().collect(Collectors.toMap(AutoMapping::description, Function.identity()));
    }
}
