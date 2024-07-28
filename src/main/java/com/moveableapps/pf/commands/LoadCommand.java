package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.loaders.CsvLoader;

import java.io.PrintStream;

public class LoadCommand implements Command {

    @Override
    public int execute(CommandArgs commandArgs, BookKeeper bookKeeper, PrintStream out, PrintStream err) {
        LoadCommandArgs args = (LoadCommandArgs) commandArgs;
        out.println("Personal Finance");
        out.println("Importing from file: " + args.inputFile);
        out.println("For account: " + args.account);
        out.println("using date format: " + args.dateFormat);
        out.println("Precision: " + args.precision + " decimal places");

        CsvLoader loader = new CsvLoader(args.inputFile, args.dateField, args.account, args.amountField, bookKeeper, args.descriptionField);
        loader.load(args.delimiter, args.skipFirstLine, args.dateFormat, args.precision);
        return 0;
    }
}
