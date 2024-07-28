package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ListAccountsCommandTest {
    private ListAccountsCommand command;
    private Repository repository;
    private BookKeeper bookKeeper;

    @BeforeEach
    void setUp() {
        command = new ListAccountsCommand();
        repository = new MemoryRepository();
        bookKeeper = new BookKeeper(repository);
    }

    private String getFirstLine() {
        return "Id\tName\tDescription\tType\n";
    }

    private String getExpectedString(long id, Account account) {
        return String.format("%d\t%s\t%s\t%s\n", id, account.name(),
                account.description(), account.type());
    }

    @Test
    @DisplayName("Command lists all the accounts")
    void listAllAccounts() {
        ListAccountsArgs args = new ListAccountsArgs();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Account account = new Account("Expense:Groceries", "Groceries", "GBP", AccountType.EXPENSE);
        long id = repository.addAccount(account);

        command.execute(args, bookKeeper, new PrintStream(stream), System.err);
        assertEquals(getFirstLine() + getExpectedString(id, account), stream.toString());
    }

    @Test
    @DisplayName("Command handles no accounts correctly")
    void listEmptyAccounts() {
        ListAccountsArgs args = new ListAccountsArgs();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        command.execute(args, bookKeeper, new PrintStream(stream), System.err);
        assertEquals(getFirstLine(), stream.toString());
    }

    @Test
    @DisplayName("Command handles empty pattern")
    void listAccountsEmptyPattern() {
        ListAccountsArgs args = new ListAccountsArgs();
        args.pattern = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Account account = new Account("Expense:Groceries", "Groceries", "GBP", AccountType.EXPENSE);
        long id = repository.addAccount(account);

        command.execute(args, bookKeeper, new PrintStream(stream), System.err);
        assertEquals(getFirstLine()  + getExpectedString(id, account), stream.toString());
    }

    @Test
    @DisplayName("Command handles pattern that results in zero results")
    void listAccountsWithPatternWithZeroResults() {
        ListAccountsArgs args = new ListAccountsArgs();
        args.pattern = "df";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Account account = new Account("Expense:Groceries", "Groceries", "GBP", AccountType.EXPENSE);
        repository.addAccount(account);

        command.execute(args, bookKeeper, new PrintStream(stream), System.err);
        assertEquals(getFirstLine() , stream.toString());
    }

    @Test
    @DisplayName("Command handles pattern that results in zero results")
    void listAccountsWithPattern() {
        ListAccountsArgs args = new ListAccountsArgs();
        args.pattern = "^.*Gr.*";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Account account = new Account("Expense:Groceries", "Groceries", "GBP", AccountType.EXPENSE);
        long id = repository.addAccount(account);
        repository.addAccount(new Account("Expense:Auto", "Auto", "GBP", AccountType.EXPENSE));

        command.execute(args, bookKeeper, new PrintStream(stream), System.err);
        assertEquals(getFirstLine() + getExpectedString(id, account), stream.toString());
    }
}
