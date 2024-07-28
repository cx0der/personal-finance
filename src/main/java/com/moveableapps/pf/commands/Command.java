package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;

import java.io.PrintStream;

public interface Command {

    int execute(CommandArgs args, BookKeeper bookKeeper, PrintStream out, PrintStream err) throws Exception;
}
