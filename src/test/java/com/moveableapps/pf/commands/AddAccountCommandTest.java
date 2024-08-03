package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddAccountCommandTest {

    private Repository repository;
    private BookKeeper bookKeeper;
    private AddAccountCommand cmd;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
        bookKeeper = new BookKeeper(repository);
        cmd = new AddAccountCommand();
    }

    @Test
    @DisplayName("Creates an account successfully")
    void addAccountCreatesSuccessfully() throws Exception {
        AddAccountCommandArgs args = new AddAccountCommandArgs("Income account", "Salary account", "INR", AccountType.INCOME);
        int exitCode = cmd.execute(args, bookKeeper, System.out, System.err);
        assertEquals(0, exitCode);
    }

    @Test
    @DisplayName("Displays the name and id of the new account")
    void addAccountPrintsNameAndId() throws Exception {
        AddAccountCommandArgs args = new AddAccountCommandArgs("Fuel", "Car fuel", "INR", AccountType.EXPENSE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        cmd.execute(args, bookKeeper, new PrintStream(stream), System.err);
        assertEquals("Created account: Fuel with id: 1\n", stream.toString());
    }

    @Test
    @DisplayName("Fails for duplicate account with exit code 1")
    void addAccountDuplicateTest() throws Exception {
        Account account = TestUtils.getIncomeAccount();
        repository.addAccount(account);
        AddAccountCommandArgs args = new AddAccountCommandArgs(account.name(), account.description(), account.currency(), account.type());
        int exitCode = cmd.execute(args, bookKeeper, System.out, System.err);
        assertEquals(1, exitCode);
    }

    @Test
    @DisplayName("Displays error message for duplicate account")
    void onDuplicatePrintsError() throws Exception {
        Account account = TestUtils.getIncomeAccount();
        repository.addAccount(account);
        AddAccountCommandArgs args = new AddAccountCommandArgs(account.name(), account.description(), account.currency(), account.type());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        cmd.execute(args, bookKeeper, System.out, new PrintStream(stream));
        assertEquals("Account Name is not unique\n", stream.toString());
    }
}
