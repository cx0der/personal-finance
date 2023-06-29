package com.moveableapps.pf.core;

import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookKeeper {

    private final Repository repository;
    private final Logger logger;

    public BookKeeper(Repository repository) {
        this(repository, LogManager.getLogger(BookKeeper.class));
    }

    public BookKeeper(Repository dbRepository, Logger logger) {
        this.repository = dbRepository;
        this.logger = logger;
    }

    public Account addAccount(Account account) {
        logger.traceEntry("addAccount({})", account);

        long accountId = repository.addAccount(account);
        Account createdAccount = account.copyWithAccountId(accountId);
        logger.traceExit(createdAccount);
        return createdAccount;
    }

    public List<Account> getAllAccounts() {
        logger.traceEntry("getAllAccounts()");
        List<Account> accounts = repository.getAllAccounts();
        logger.traceExit(accounts);
        return accounts;
    }

    public Optional<Account> getAccountByName(String name) {
        logger.traceEntry("getAccountByName({})", name);
        Optional<Account> account = repository.getAccountByName(name);
        logger.traceExit(account);
        return account;
    }

    public AutoMapping addAutoMapping(AutoMapping autoMapping) {
        logger.traceEntry("addAutoMapping({})", autoMapping);
        long id = repository.addAutoMapping(autoMapping);
        AutoMapping result = autoMapping.copyWithMappingId(id);
        logger.traceExit(result);
        return result;
    }

    public Map<String, AutoMapping> getAllAutoMappings() {
        logger.traceEntry("getAllAutoMappings()");
        Map<String, AutoMapping> mappings = repository.getAllAutoMappings();
        logger.traceExit(mappings);
        return mappings;
    }

    public Transaction addTransaction(Transaction transaction, List<Split> splits) {
        logger.traceEntry("addTransaction({}, {})", transaction, splits);
        long transactionId = repository.addTransaction(transaction);
        repository.addSplits(transactionId, splits);
        logger.traceExit(transactionId);
        return transaction.copyWithTransactionId(transactionId);
    }

    public List<Split> getTransactionSplits(long transactionId) {
        logger.traceEntry("getTransactionSplits({})", transactionId);

        List<Split> splits = repository.getSplitsForTransaction(transactionId);
        logger.traceExit(splits);
        return splits;
    }
}
