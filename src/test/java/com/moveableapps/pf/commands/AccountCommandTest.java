package com.moveableapps.pf.commands;

import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountCommandTest {

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
    }

    @Test
    void addAccountBaseTest() {
        AccountCommandArgs args = new AccountCommandArgs("Income account", "Salary account", "INR", AccountType.INCOME);
        AccountCommand cmd = new AccountCommand(repository, args);
        int exitCode = cmd.execute();
        assertEquals(0, exitCode);
    }

    @Test
    void listAccountTest() {
        AccountCommandArgs args = new AccountCommandArgs(true);
        AccountCommand cmd = new AccountCommand(repository, args);
        int exitCode = cmd.execute();
        assertEquals(0, exitCode);
    }
}
