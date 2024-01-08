package com.moveableapps.pf.core;

import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookKeeper {

    private final Repository repository;

    public BookKeeper(Repository repository) {
        this.repository = repository;
    }

    public Account addAccount(Account account) {

        long accountId = repository.addAccount(account);
        return account.copyWithAccountId(accountId);
    }

    public List<Account> getAllAccounts() {
        return repository.getAllAccounts();
    }

    public Optional<Account> getAccountByName(String name) {
        return repository.getAccountByName(name);
    }

    public AutoMapping addAutoMapping(AutoMapping autoMapping) {
        long id = repository.addAutoMapping(autoMapping);
        return autoMapping.copyWithMappingId(id);
    }

    public Map<String, AutoMapping> getAllAutoMappings() {
        return repository.getAllAutoMappings();
    }

    public Transaction addTransaction(Transaction transaction, List<Split> splits) {
        long transactionId = repository.addTransaction(transaction);
        repository.addSplits(transactionId, splits);
        return transaction.copyWithTransactionId(transactionId);
    }

    public List<Split> getTransactionSplits(long transactionId) {

        return repository.getSplitsForTransaction(transactionId);
    }
}
