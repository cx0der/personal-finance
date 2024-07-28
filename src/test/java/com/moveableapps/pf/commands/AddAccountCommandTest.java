package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
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

class AddAccountCommandTest {

    private Repository repository;
    private BookKeeper bookKeeper;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
        bookKeeper = new BookKeeper(repository);
    }

    @Test
    void addAccount_returnsZero_goodCase() {
        AddAccountCommandArgs args = new AddAccountCommandArgs("Income account", "Salary account", "INR", AccountType.INCOME);
        AddAccountCommand cmd = new AddAccountCommand();
        int exitCode = cmd.execute(args, bookKeeper, System.out, System.err);
        assertEquals(0, exitCode);
    }

    @Test
    void addAccount_existsWithOne_onDuplicateName() {
        Account account = TestUtils.getIncomeAccount();
        repository.addAccount(account);
        AddAccountCommandArgs args = new AddAccountCommandArgs(account.name(), account.description(), account.currency(), account.type());
        AddAccountCommand cmd = new AddAccountCommand();
        int exitCode = cmd.execute(args, bookKeeper, System.out, System.err);
        assertEquals(1, exitCode);
    }

    @Test
    void addAccount_printsError_onDuplicateName() {
        Account account = TestUtils.getIncomeAccount();
        repository.addAccount(account);
        AddAccountCommandArgs args = new AddAccountCommandArgs(account.name(), account.description(), account.currency(), account.type());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        AddAccountCommand cmd = new AddAccountCommand();
        cmd.execute(args, bookKeeper, System.out, new PrintStream(stream));
        assertEquals("Account Name is not unique\n", stream.toString());
    }
}
