package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.DBRepository;
import com.moveableapps.pf.loaders.CsvLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;

@Command(name = "load", description = "Load a file")
public class LoadCommand implements Runnable {
    private static final Logger logger = LogManager.getLogger(LoadCommand.class);

    @Option(names = "-f", paramLabel = "FILE", description = "Path of the file to import", required = true)
    private String inputFile;

    @Option(names = {"-a", "--account"}, paramLabel = "ACCOUNT", required = true, description = "Account name")
    private String account;

    @Option(names = {"-d", "--date"}, description = "Date format. Default: dd/mm/yyyy", defaultValue = "dd/mm/yyyy")
    private String dateFormat;

    @Option(names = {"-l", "--delimiter"}, description = "CSV field delimiter", defaultValue = ",")
    private Character delimiter;

    @Option(names = {"-s", "--skip-first"}, description = "Skip first line", defaultValue = "true")
    private boolean skipFirstLine;

    @Option(names = "--date-field", description = "Column with Date", required = true)
    private int dateField;

    @Option(names = "--amount-field", description = "Column with the amount", required = true)
    private int amountField;

    @Option(names = "--desc-field", description = "Description column", required = true)
    private int descriptionField;

    @Option(names = {"p", "--precision"}, description = "Number of decimal places", defaultValue = "2")
    private int precision;

    @Override
    public void run() {
        logger.info("Personal Finance");
        logger.info("Importing from file: " + inputFile);
        logger.info("For account: " + account);
        logger.info("using date format: " + dateFormat);
        logger.info("Precision: " + precision + " decimal places");
        BookKeeper bookKeeper = new BookKeeper(new DBRepository(getDatabasePath()));

        CsvLoader loader = new CsvLoader(inputFile, dateField, account, amountField, bookKeeper, descriptionField);
        loader.load(delimiter, skipFirstLine, dateFormat, precision);
    }

    private String getDatabasePath() {
        String homeDirectory = System.getenv("HOME");
        if (homeDirectory == null) {
            logger.error("Unable to get environment variable HOME");
            throw new RuntimeException("Unable to get environment variable HOME");
        }

        String xdgDataHome = System.getenv("XDG_DATA_HOME");
        if (xdgDataHome == null || xdgDataHome.isBlank()) {
            xdgDataHome = Path.of(homeDirectory, ".local", "share").toString();
        }

        return Path.of(xdgDataHome, "pf").toString();
    }
}
