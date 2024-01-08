package com.moveableapps.pf.commands;

import com.beust.jcommander.Parameter;

public class LoadCommandArgs {

    @Parameter(names = "-f", description = "Path of the file to import", required = true)
    String inputFile;

    @Parameter(names = {"-a", "--account"}, required = true, description = "Account name")
    String account;

    @Parameter(names = {"-d", "--date"}, description = "Date format to use")
    String dateFormat = "dd/mm/yyyy";

    @Parameter(names = {"-l", "--delimiter"}, description = "CSV field delimiter")
    char delimiter = ',';

    @Parameter(names = {"-s", "--skip-first"}, description = "Skip first line")
    boolean skipFirstLine = true;

    @Parameter(names = "--date-field", description = "Column with Date", required = true)
    int dateField;

    @Parameter(names = "--amount-field", description = "Column with the amount", required = true)
    int amountField;

    @Parameter(names = "--desc-field", description = "Description column", required = true)
    int descriptionField;

    @Parameter(names = {"p", "--precision"}, description = "Number of decimal places")
    int precision = 2;

    public LoadCommandArgs() {
        // default constructor
    }

    public LoadCommandArgs(String inputFile, String account, String dateFormat, char delimiter,
                           boolean skipFirstLine, int dateField, int amountField, int descriptionField, int precision) {
        this.inputFile = inputFile;
        this.account = account;
        this.dateFormat = dateFormat;
        this.delimiter = delimiter;
        this.skipFirstLine = skipFirstLine;
        this.dateField = dateField;
        this.amountField = amountField;
        this.descriptionField = descriptionField;
        this.precision = precision;
    }
}
