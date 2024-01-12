package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.loaders.CsvLoader;

import java.io.PrintStream;

public class LoadCommand extends Command {

    private final Repository repository;
    private final LoadCommandArgs args;

    public LoadCommand(Repository repository, LoadCommandArgs loadCommandArgs, PrintStream out, PrintStream err) {
        super(out, err);
        this.repository = repository;
        this.args = loadCommandArgs;
    }

    @Override
    public int execute() {
        out.println("Personal Finance");
        out.println("Importing from file: " + args.inputFile);
        out.println("For account: " + args.account);
        out.println("using date format: " + args.dateFormat);
        out.println("Precision: " + args.precision + " decimal places");
        BookKeeper bookKeeper = new BookKeeper(repository);

        CsvLoader loader = new CsvLoader(args.inputFile, args.dateField, args.account, args.amountField, bookKeeper, args.descriptionField);
        loader.load(args.delimiter, args.skipFirstLine, args.dateFormat, args.precision);
        return 0;
    }
}
