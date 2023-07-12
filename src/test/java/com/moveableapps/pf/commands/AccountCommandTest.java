package com.moveableapps.pf.commands;

import com.moveableapps.pf.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountCommandTest {

    private CommandLine cmd;

    @BeforeEach
    void setUp() {
        cmd = new CommandLine(new Main());
    }

    @Test
    void addAccountBaseTest() {
        int exitCode = cmd.execute("--repo=memory", "account", "-a='test account'", "-c=INR", "-d='income account'", "-t=INCOME");
        assertEquals(0, exitCode);
    }
}