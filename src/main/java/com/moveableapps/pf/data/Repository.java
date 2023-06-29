package com.moveableapps.pf.data;

import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Repository {

    long addAccount(Account account);

    List<Account> getAllAccounts();

    Optional<Account> getAccountByName(String name);

    long addTransaction(Transaction transaction);

    List<Transaction> getAllTransactions();

    int addSplits(long transactionId, List<Split> splits);

    List<Split> getSplitsForTransaction(long transactionId);

    long addAutoMapping(AutoMapping mapping);

    Map<String, AutoMapping> getAllAutoMappings();
}
