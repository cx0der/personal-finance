package com.moveableapps.pf.data;

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

class MemoryRepositoryTest {

    private Repository memoryRepo;

    @BeforeEach
    void setUp() {
        memoryRepo = new MemoryRepository();
    }

    @Test
    void addAccountTest() {
        Account account = new Account("Test account", "test desc", "INR", AccountType.INCOME);
        long accountId = memoryRepo.addAccount(account);
        assertEquals(1, accountId);
    }

    @Test
    void getAllAccountsTest() {
        Account account1 = new Account("Income", "test desc", "INR", AccountType.INCOME);
        Account account2 = new Account("Expense", "test desc", "INR", AccountType.EXPENSE);
        memoryRepo.addAccount(account1);
        memoryRepo.addAccount(account2);

        assertEquals(2, memoryRepo.getAllAccounts().size());
    }

    @Test
    void getAccountByNameTest() {
        Account account1 = new Account("Income", "test desc", "INR", AccountType.INCOME);
        Account account2 = new Account("Expense", "test desc", "INR", AccountType.EXPENSE);
        long accountId = memoryRepo.addAccount(account1);
        memoryRepo.addAccount(account2);

        Optional<Account> result = memoryRepo.getAccountByName("Income");
        assertTrue(result.isPresent());
        assertTrue(result.get().id().isPresent());
        assertEquals(accountId, result.get().id().getAsLong());
    }

    @Test
    void addTransactionTest() {
        Date now = Date.from(Instant.now());
        Transaction transaction = new Transaction(now, now, "first txn");

        long id = memoryRepo.addTransaction(transaction);

        assertEquals(1, id);
    }

    @Test
    void getTransactionsTest() {
        Date now = Date.from(Instant.now());
        Transaction transaction = new Transaction(now, now, "first txn");
        Transaction transaction2 = new Transaction(now, now, "second txn");

        memoryRepo.addTransaction(transaction);
        memoryRepo.addTransaction(transaction2);

        assertEquals(2, memoryRepo.getAllTransactions().size());
    }

    @Test
    void addSplitTest() {
        Split split = new Split(1, 'y', 1000, 100);
        Split split2 = new Split(2, 'y', -1000, 100);

        int count = memoryRepo.addSplits(1, List.of(split, split2));
        assertEquals(2, count);
    }

    @Test
    void getSplitsForTransactionTest() {
        Split split1 = new Split(1, 'y', 1000, 100);
        Split split2 = new Split(2, 'y', -1000, 100);
        long transactionId = 100;
        memoryRepo.addSplits(transactionId, List.of(split1, split2));

        Split split3 = new Split(1, 'y', 1000, 100);
        Split split4 = new Split(2, 'y', -1000, 100);
        long otherTransactionId = 200;
        memoryRepo.addSplits(otherTransactionId, List.of(split3, split4));

        List<Split> result = memoryRepo.getSplitsForTransaction(transactionId);
        assertEquals(2, result.size());
        assertTrue(result.get(0).txnId().isPresent());
        assertEquals(100, result.get(0).txnId().getAsLong());
    }

    @Test
    void addAutoMappingTest() {
        AutoMapping mapping = new AutoMapping("Some description", 1);
        long mappingId = memoryRepo.addAutoMapping(mapping);

        assertEquals(1, mappingId);
    }

    @Test
    void getAllAutoMappingsTest() {
        String description = "Some description";
        AutoMapping mapping = new AutoMapping(description, 1);
        long mappingId = memoryRepo.addAutoMapping(mapping);

        Map<String, AutoMapping> result = memoryRepo.getAllAutoMappings();
        assertEquals(1, result.size());
        assertNotNull(result.get(description));
        assertEquals(mapping.copyWithMappingId(mappingId), result.get(description));
    }
}