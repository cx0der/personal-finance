package com.moveableapps.pf.commands;

import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingCommandTest {

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
    }

    @Test
    void listMappings_exitsWithZero() {
        repository.addAutoMapping(TestUtils.getTestAutoMapping());
        MappingCommandArgs args = new MappingCommandArgs(true);
        MappingCommand cmd = new MappingCommand(repository, args, System.out, System.err);

        int exitCode = cmd.execute();

        assertEquals(0, exitCode);
    }

    @Test
    void listMappings_printsCorrectly() {
        AutoMapping mapping = TestUtils.getTestAutoMapping();
        repository.addAutoMapping(mapping);
        String expected = String.format("Txn Description\tMapped Account\n%s\t%d\n", mapping.description(), mapping.accountId());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        MappingCommandArgs args = new MappingCommandArgs(true);
        MappingCommand cmd = new MappingCommand(repository, args, new PrintStream(stream), System.err);
        cmd.execute();

        assertEquals(expected, stream.toString());
    }

    @Test
    void addMapping_addsMapping() {
        Account account = new Account("Expense:Groceries", "Groceries", "INR", AccountType.EXPENSE);
        repository.addAccount(account);
        MappingCommandArgs args = new MappingCommandArgs("groceries", "Expense:Groceries");
        MappingCommand cmd = new MappingCommand(repository, args, System.out, System.err);
        cmd.execute();

        Map<String, AutoMapping> mappings = repository.getAllAutoMappings();
        assertEquals(1, mappings.size());
    }

    @Test
    void addMapping_throwsException_forDuplicateDescription() {
        Account account = new Account("Expense:Groceries", "Groceries", "INR", AccountType.EXPENSE);
        repository.addAccount(account);
        AutoMapping mapping = new AutoMapping("groceries", 1);
        repository.addAutoMapping(mapping);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        MappingCommandArgs args = new MappingCommandArgs("groceries", "Expense:Groceries");
        MappingCommand cmd = new MappingCommand(repository, args, System.out, new PrintStream(stream));
        cmd.execute();

        assertEquals("Description is not unique\n", stream.toString());

        Map<String, AutoMapping> mappings = repository.getAllAutoMappings();
        assertEquals(1, mappings.size());
    }
}
