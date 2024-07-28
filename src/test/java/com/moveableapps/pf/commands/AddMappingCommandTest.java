package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.entities.AutoMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddMappingCommandTest {

    private Repository repository;
    private BookKeeper bookKeeper;
    AddMappingCommand cmd;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
        bookKeeper = new BookKeeper(repository);
        cmd = new AddMappingCommand();
    }

    @Test
    @DisplayName("Successfully adds a valid mapping")
    void addValidMappingTest() {
        Account account = new Account("Expense:Groceries", "Groceries", "INR", AccountType.EXPENSE);
        repository.addAccount(account);
        AddMappingCommandArgs args = new AddMappingCommandArgs("groceries", "Expense:Groceries");
        cmd.execute(args, bookKeeper, System.out, System.err);

        Map<String, AutoMapping> mappings = repository.getAllAutoMappings();
        assertEquals(1, mappings.size());
    }

    @Test
    @DisplayName("Fails for duplicate description")
    void failForDuplicateDescription() {
        Account account = new Account("Expense:Groceries", "Groceries", "INR", AccountType.EXPENSE);
        repository.addAccount(account);

        AutoMapping mapping = new AutoMapping("groceries", 1);
        repository.addAutoMapping(mapping);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        AddMappingCommandArgs args = new AddMappingCommandArgs("groceries", "Expense:Groceries");
        int exitCode = cmd.execute(args, bookKeeper, System.out, new PrintStream(stream));

        assertEquals("Description is not unique\n", stream.toString());
        assertEquals(1, exitCode);

        Map<String, AutoMapping> mappings = repository.getAllAutoMappings();
        assertEquals(1, mappings.size());
    }

    @Test
    @DisplayName("Fails if unable to find account")
    void failForInvalidAccount() {
        AddMappingCommandArgs args = new AddMappingCommandArgs("groceries", "Expense:Groceries");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int exitCode = cmd.execute(args, bookKeeper, System.out, new PrintStream(stream));

        assertEquals(1, exitCode);
        assertEquals("Cannot find account by the name: Expense:Groceries\n", stream.toString());

        Map<String, AutoMapping> mappings = repository.getAllAutoMappings();
        assertEquals(0, mappings.size());
    }
}
