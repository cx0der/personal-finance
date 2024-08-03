package com.moveableapps.pf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    private Main main;

    @BeforeEach
    void setUp() {
        main = new Main();
    }

    @Test
    @DisplayName("Executes a sub-command")
    void executesASubCommandCorrectly() {
        String[] args = {"--repo", "mem", "list-accounts"};
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int exitCode = main.run(args, new PrintStream(stream), System.err);
        assertEquals(0, exitCode);
        assertEquals("Id\tName\tDescription\tType\n", stream.toString());
    }

    @Test
    @DisplayName("Exits with code One for invalid args")
    void exitWithCodeOne() {
        String[] args = {"--repo", "mem", "list-accounts", "-a"};
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int exitCode = main.run(args, System.out, new PrintStream(stream));
        assertEquals(1, exitCode);
        assertEquals("Was passed main parameter '-a' but no main parameter was defined in your arg class\n", stream.toString());
    }

    @Test
    @DisplayName("Exits with code two for no sub commands")
    void exitForNoSubCommand() {
        String[] args = {"--repo", "mem"};
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int exitCode = main.run(args, System.out, new PrintStream(stream));
        assertEquals(2, exitCode);
        assertEquals("Sub command is required.\n", stream.toString());
    }

    @Test
    @DisplayName("Exits with code 127 if the sub command fails")
    void exitForSubCommandFailure() {
        String[] args = {"--repo", "mem", "add-mapping", "--desc", "sdf", "--target", "Expense:Groceries"};

        int exitCode = main.run(args, System.out, System.err);
        assertEquals(127, exitCode);
    }
}
