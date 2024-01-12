package com.moveableapps.pf.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.moveableapps.pf.entities.AccountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountCommandArgsTest {

    @Test
    void should_throwException_when_neitherListOrAddIsGiven() {
        String[] args = {"account"};
        AccountCommandArgs cmdArgs = new AccountCommandArgs();
        JCommander jc = JCommander.newBuilder()
                .addCommand("account", cmdArgs)
                .build();
        assertThrows(ParameterException.class, () -> jc.parse(args));

    }

    @Test
    void should_pass_when_onlyListIsGiven() {
        String[] args = {"account", "-l"};
        AccountCommandArgs cmdArgs = new AccountCommandArgs();
        JCommander jc = JCommander.newBuilder()
                .addCommand("account", cmdArgs)
                .build();
        jc.parse(args);

        assertTrue(cmdArgs.listAccounts);
    }

    @Test
    void should_throwException_when_bothListAndAddGiven() {
        String[] args = {"account", "-l", "-a", "name"};
        AccountCommandArgs cmdArgs = new AccountCommandArgs();
        JCommander jc = JCommander.newBuilder()
                .addCommand("account", cmdArgs)
                .build();
        assertThrows(ParameterException.class, () -> jc.parse(args));
    }

    @Test
    void should_pass_when_allAddArgsAreGiven() {
        String[] args = {"account", "-a", "ac name", "-c", "INR", "-t", "INCOME"};
        AccountCommandArgs cmdArgs = new AccountCommandArgs();
        JCommander jc = JCommander.newBuilder()
                .addCommand("account", cmdArgs)
                .build();
        jc.parse(args);
        assertEquals("ac name", cmdArgs.accountName);
        assertEquals("INR", cmdArgs.currency);
        assertEquals(AccountType.INCOME, cmdArgs.accountType);
    }

    @Test
    void should_throwException_when_notEnoughAddArgsAreGiven() {
        String[] args = {"account", "-a", "ac name"};
        AccountCommandArgs cmdArgs = new AccountCommandArgs();
        JCommander jc = JCommander.newBuilder()
                .addCommand("account", cmdArgs)
                .build();
        assertThrows(ParameterException.class, () -> jc.parse(args));
    }
}
