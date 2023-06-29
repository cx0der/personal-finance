package com.moveableapps.pf.data;

import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBRepositoryTest {

    @TempDir
    private Path tempDir;

    private DBRepository dbRepository;

    @BeforeEach
    void setUp() {
        String dbPath = tempDir.resolve("").toAbsolutePath().toString();
        dbRepository = new DBRepository(dbPath);
    }

    @Test
    void addAccountTest() {
        Account account = new Account("Test account", "test desc", "INR", AccountType.INCOME);
        long accountId = dbRepository.addAccount(account);
        assertTrue(accountId > 0);
    }

    @Test
    void getAllAccountsTest() {
        Account account1 = new Account("Income", "test desc", "INR", AccountType.INCOME);
        Account account2 = new Account("Expense", "test desc", "INR", AccountType.EXPENSE);
        dbRepository.addAccount(account1);
        dbRepository.addAccount(account2);

        List<Account> accounts = dbRepository.getAllAccounts();
        assertEquals(2, accounts.size());
    }

    @Test
    void getAccountByNameTest() {
        Account account1 = new Account("Income", "test desc", "INR", AccountType.INCOME);
        Account account2 = new Account("Expense", "test desc", "INR", AccountType.EXPENSE);
        long accountId = dbRepository.addAccount(account1);
        dbRepository.addAccount(account2);

        Optional<Account> mayBeAccount = dbRepository.getAccountByName("income");
        assertTrue(mayBeAccount.isPresent());
        assertTrue(mayBeAccount.get().id().isPresent());
        assertEquals(accountId, mayBeAccount.get().id().getAsLong());
    }

    @Test
    void addTransactionTest() {
        Date now = Date.from(Instant.now());
        Transaction transaction = new Transaction(now, now, "first txn");

        long transactionId = dbRepository.addTransaction(transaction);

        List<Transaction> transactions = dbRepository.getAllTransactions();

        assertEquals(1, transactions.size());
        Transaction result = transactions.get(0);
        assertNotNull(result);
        assertTrue(result.id().isPresent());
        assertEquals(transactionId, result.id().getAsLong());
    }

    @Test
    void getSplitsForTransactionTest() {
        Date now = Date.from(Instant.now());
        Transaction transaction = new Transaction(now, now, "first txn");
        long transactionId = dbRepository.addTransaction(transaction);

        Split s1 = new Split(1, 'y', now, 10000, 100);
        Split s2 = new Split(2, 'y', now, -10000, 100);
        dbRepository.addSplits(transactionId, List.of(s1, s2));

        List<Split> result = dbRepository.getSplitsForTransaction(transactionId);

        assertEquals(2, result.size());
        Split rS1 = result.get(0);
        assertTrue(rS1.txnId().isPresent());
        assertEquals(transactionId, rS1.txnId().getAsLong());
        assertEquals(10000, rS1.valueNum());
    }

    @Test
    void addAutoMappingTest() {
        AutoMapping mapping = new AutoMapping("some description", 1);

        long id = dbRepository.addAutoMapping(mapping);

        assertTrue(id > 0);
    }

    @Test
    void getAllAutoMappingTest() {
        AutoMapping m1 = new AutoMapping("some description", 1);
        dbRepository.addAutoMapping(m1);
        AutoMapping m2 = new AutoMapping("some other description", 2);
        dbRepository.addAutoMapping(m2);

        Map<String, AutoMapping> result = dbRepository.getAllAutoMappings();
        assertEquals(2, result.size());
        assertEquals(1, result.get("some description").accountId());
    }
}
