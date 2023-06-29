package com.moveableapps.pf.core;

import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookKeeperTest {

    private Repository inMemoryRepository;
    private BookKeeper bookKeeper;

    @BeforeEach
    void setUp() {
        inMemoryRepository = new MemoryRepository();
        bookKeeper = new BookKeeper(inMemoryRepository);
    }

    @Test
    void addAccountTest() {
        Account account = new Account("Income account", "salary account", "GBP", AccountType.INCOME);

        Account result = bookKeeper.addAccount(account);

        assertTrue(result.id().isPresent());
        assertEquals(AccountType.INCOME, result.type());
    }

    @Test
    void getAllAccountsTest() {
        Account account = new Account("Income account", "salary account", "GBP", AccountType.INCOME);
        bookKeeper.addAccount(account);

        List<Account> result = bookKeeper.getAllAccounts();
        assertEquals(1, result.size());
    }

    @Test
    void getAccountByNameTest() {
        Account account = new Account("Income account", "salary account", "GBP", AccountType.INCOME);
        bookKeeper.addAccount(account);
        Account account2 = new Account("Expense account", "Credit card", "GBP", AccountType.EXPENSE);
        bookKeeper.addAccount(account2);

        Optional<Account> result = bookKeeper.getAccountByName("Income account");

        assertTrue(result.isPresent());
        assertEquals("Income account", result.get().name());
    }

    @Test
    void addAutoMappingTest() {
        AutoMapping mapping = new AutoMapping("a description", 1);
        AutoMapping result = bookKeeper.addAutoMapping(mapping);

        assertTrue(result.id().isPresent());
        assertTrue(result.id().getAsLong() > 0);
    }

    @Test
    void getAllAutoMappingsTest() {
        AutoMapping mapping = new AutoMapping("a description", 1);
        bookKeeper.addAutoMapping(mapping);

        Map<String, AutoMapping> result = bookKeeper.getAllAutoMappings();
        assertEquals(1, result.size());
        assertNotNull(result.get("a description"));
        assertEquals(1, result.get("a description").accountId());
    }

    @Test
    void addTransactionTest() {
        Date now = Date.from(Instant.now());
        Transaction transaction = new Transaction(now, now, "Deposit 1000");

        Split credit = new Split(1, 'y', 100000, 100);
        Split debit = new Split(2, 'y', -100000, 100);

        Transaction addedTransaction = bookKeeper.addTransaction(transaction, List.of(credit, debit));

        List<Transaction> allTransactions = inMemoryRepository.getAllTransactions();
        assertEquals(1, allTransactions.size());
        assertTrue(addedTransaction.id().isPresent());
        assertTrue(allTransactions.get(0).id().isPresent());
        assertEquals(addedTransaction.id().getAsLong(), allTransactions.get(0).id().getAsLong());

        List<Split> allSplits = inMemoryRepository.getSplitsForTransaction(addedTransaction.id().getAsLong());
        assertEquals(2, allSplits.size());
        assertTrue(allSplits.get(0).txnId().isPresent());
        assertEquals(addedTransaction.id().getAsLong(), allSplits.get(0).txnId().getAsLong());
    }

    @Test
    void getTransactionSplitsTest() {
        Date now = Date.from(Instant.now());
        Transaction transaction = new Transaction(now, now, "Deposit 1000");

        Split credit = new Split(1, 'y', 100000, 100);
        Split debit = new Split(2, 'y', -100000, 100);

        Transaction addedTransaction = bookKeeper.addTransaction(transaction, List.of(credit, debit));

        assertTrue(addedTransaction.id().isPresent());
        List<Split> result = bookKeeper.getTransactionSplits(addedTransaction.id().getAsLong());

        assertEquals(2, result.size());
    }
}
