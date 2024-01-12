package com.moveableapps.pf.commands;

import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountCommandTest {

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
    }

    @Test
    void addAccount_returnsZero_goodCase() {
        AccountCommandArgs args = new AccountCommandArgs("Income account", "Salary account", "INR", AccountType.INCOME);
        AccountCommand cmd = new AccountCommand(repository, args, System.out, System.err);
        int exitCode = cmd.execute();
        assertEquals(0, exitCode);
    }

    @Test
    void addAccount_existsWithOne_onDuplicateName() {
        Account account = TestUtils.getIncomeAccount();
        repository.addAccount(account);
        AccountCommandArgs args = new AccountCommandArgs(account.name(), account.description(), account.currency(), account.type());
        AccountCommand cmd = new AccountCommand(repository, args, System.out, System.err);
        int exitCode = cmd.execute();
        assertEquals(1, exitCode);
    }

    @Test
    void addAccount_printsError_onDuplicateName() {
        Account account = TestUtils.getIncomeAccount();
        repository.addAccount(account);
        AccountCommandArgs args = new AccountCommandArgs(account.name(), account.description(), account.currency(), account.type());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        AccountCommand cmd = new AccountCommand(repository, args, System.out, new PrintStream(stream));
        cmd.execute();
        assertEquals("Account Name is not unique\n", stream.toString());
    }

    @Test
    void listAccounts_exitsWithZero() {
        AccountCommandArgs args = new AccountCommandArgs(true);
        AccountCommand cmd = new AccountCommand(repository, args, System.out, System.err);
        int exitCode = cmd.execute();
        assertEquals(0, exitCode);
    }

    @Test
    void listAccounts_listsAllAccounts() {
        Account account = TestUtils.getIncomeAccount();
        repository.addAccount(account);
        String expected = String.format("Id\tName\tDescription\tType\n1\t%s\t%s\t%s\n", account.name(), account.description(), account.type());

        AccountCommandArgs args = new AccountCommandArgs(true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        AccountCommand cmd = new AccountCommand(repository, args, new PrintStream(stream), System.err);
        cmd.execute();

        assertEquals(expected, stream.toString());
    }
}
