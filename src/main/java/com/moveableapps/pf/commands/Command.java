package com.moveableapps.pf.commands;

import java.io.PrintStream;

public abstract class Command {

    protected final PrintStream out;
    protected final PrintStream err;

    public Command(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    public abstract int execute() throws Exception;
}
