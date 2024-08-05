package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Transaction;
import com.moveableapps.pf.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadCommandTest {

    @TempDir
    Path inputDirectory;

    private LoadCommand command;
    private Repository repository;
    private BookKeeper bookKeeper;
    private Account incomeAccount;

    @BeforeEach
    void init() {
        incomeAccount = TestUtils.getIncomeAccount();
        repository = new MemoryRepository();
        repository.addAccount(incomeAccount);
        bookKeeper = new BookKeeper(repository);
        command = new LoadCommand();
    }

    @Test
    @DisplayName("Successfully loads an empty file")
    void loadsEmptyFile() throws IOException {
        File inputFile = inputDirectory.resolve("input.csv").toFile();
        //noinspection ResultOfMethodCallIgnored
        inputFile.createNewFile();

        LoadCommandArgs args = TestUtils.getLoadArgsWithDefaults(inputFile.getAbsolutePath(), incomeAccount.name(),
                0, 2, 1);

        assertDoesNotThrow(() -> command.execute(args, bookKeeper, System.out, System.err));
    }

    @Test
    @DisplayName("Successfully loads a file with one transaction")
    void loadsFileWithOneTransaction() throws IOException {
        // Setup
        File inputFile = inputDirectory.resolve("one_txn.csv").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("Description, Date, Amount\n");
            writer.write("food mart, 01/08/2024, 25.35\n");
        }

        Account account = TestUtils.getExpenseAccount();
        long id = repository.addAccount(account);

        repository.addAutoMapping(new AutoMapping("food mart", id));

        LoadCommandArgs args = TestUtils.getLoadArgsWithDefaults(inputFile.getAbsolutePath(), incomeAccount.name(),
                2, 3, 1);

        // Execute
        assertDoesNotThrow(() -> command.execute(args, bookKeeper, System.out, System.err));

        // Verify
        List<Transaction> transactions = repository.getAllTransactions();
        assertEquals(1, transactions.size());
        //noinspection OptionalGetWithoutIsPresent
        assertEquals(2, repository.getSplitsForTransaction(transactions.get(0).id().getAsLong()).size());
    }

    @Test
    @DisplayName("Successfully loads a file with one line")
    void loadsFileWithOneLine() throws IOException {
        // Setup
        File inputFile = inputDirectory.resolve("one_line.csv").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("food mart, 01/08/2024, 25.35\n");
        }

        Account account = TestUtils.getExpenseAccount();
        long id = repository.addAccount(account);

        repository.addAutoMapping(new AutoMapping("food mart", id));

        LoadCommandArgs args = TestUtils.getLoadArgsWithDefaults(inputFile.getAbsolutePath(), incomeAccount.name(),
                2, 3, 1);
        args.skipFirstLine = false;

        // Execute
        assertDoesNotThrow(() -> command.execute(args, bookKeeper, System.out, System.err));

        // Verify
        List<Transaction> transactions = repository.getAllTransactions();
        assertEquals(1, transactions.size());
        //noinspection OptionalGetWithoutIsPresent
        assertEquals(2, repository.getSplitsForTransaction(transactions.get(0).id().getAsLong()).size());
    }

    @Test
    @DisplayName("Dry run does not import transactions")
    void dryRunDoesNotImport() throws IOException {
        // Setup
        File inputFile = inputDirectory.resolve("one_line_dry_run.csv").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("food mart, 01/08/2024, 25.35\n");
        }

        Account account = TestUtils.getExpenseAccount();
        long id = repository.addAccount(account);

        repository.addAutoMapping(new AutoMapping("food mart", id));

        LoadCommandArgs args = TestUtils.getLoadArgsWithDefaults(inputFile.getAbsolutePath(), incomeAccount.name(),
                2, 3, 1);
        args.skipFirstLine = false;
        args.dryRunOnly = true;

        // Execute
        assertDoesNotThrow(() -> command.execute(args, bookKeeper, System.out, System.err));

        // Verify
        assertTrue(repository.getAllTransactions().isEmpty());
    }
}
