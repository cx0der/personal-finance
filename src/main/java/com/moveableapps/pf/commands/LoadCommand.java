package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.loaders.CsvLoader;

public class LoadCommand implements Command {

    private final Repository repository;
    private final LoadCommandArgs args;

    public LoadCommand(Repository repository, LoadCommandArgs loadCommandArgs) {
        this.repository = repository;
        this.args = loadCommandArgs;
    }

    @Override
    public int execute() {
//        logger.info("Personal Finance");
//        logger.info("Importing from file: " + inputFile);
//        logger.info("For account: " + account);
//        logger.info("using date format: " + dateFormat);
//        logger.info("Precision: " + precision + " decimal places");
        BookKeeper bookKeeper = new BookKeeper(repository);

        CsvLoader loader = new CsvLoader(args.inputFile, args.dateField, args.account, args.amountField, bookKeeper, args.descriptionField);
        loader.load(args.delimiter, args.skipFirstLine, args.dateFormat, args.precision);
        return 0;
    }
}
