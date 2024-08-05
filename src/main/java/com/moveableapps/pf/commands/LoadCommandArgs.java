package com.moveableapps.pf.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.io.File;
import java.util.Map;

@Parameters(commandDescription = "Load transactions", parametersValidators = LoadCommandArgs.class)
public class LoadCommandArgs implements CommandArgs, IParametersValidator {

    public static final String DEFAULT_DATE_FORMAT = "dd/mm/yyyy";
    public static final char DEFAULT_DELIMITER = ',';
    public static final boolean DEFAULT_SKIP_FIRST_LINE = true;
    public static final boolean DEFAULT_DRY_RUN_ONLY = false;
    public static final int DEFAULT_PRECISION = 2;


    @Parameter(names = "-f", description = "Path of the file to import", required = true)
    String inputFile;

    @Parameter(names = {"-a", "--account"}, description = "Account name", required = true)
    String account;

    @Parameter(names = "--date-field", description = "Column with Date", required = true)
    int dateField;

    @Parameter(names = "--amount-field", description = "Column with the amount", required = true)
    int amountField;

    @Parameter(names = "--desc-field", description = "Description column", required = true)
    int descriptionField;

    // options with default values
    @Parameter(names = {"-d", "--date"}, description = "Date format to use")
    String dateFormat = DEFAULT_DATE_FORMAT;

    @Parameter(names = {"-l", "--delimiter"}, description = "CSV field delimiter")
    char delimiter = DEFAULT_DELIMITER;

    @Parameter(names = {"-s", "--skip-first"}, description = "Skip first line")
    boolean skipFirstLine = DEFAULT_SKIP_FIRST_LINE;

    @Parameter(names = {"-n", "--dry-run"}, description = "Dry run only do not load")
    boolean dryRunOnly = DEFAULT_DRY_RUN_ONLY;

    @Parameter(names = {"p", "--precision"}, description = "Number of decimal places")
    int precision = DEFAULT_PRECISION;

    public LoadCommandArgs() {
        // default constructor
    }

    public LoadCommandArgs(String inputFile, String account, int dateField, int amountField, int descriptionField) {
        this(inputFile, account, dateField, amountField, descriptionField, DEFAULT_DATE_FORMAT, DEFAULT_DELIMITER,
                DEFAULT_SKIP_FIRST_LINE, DEFAULT_DRY_RUN_ONLY, DEFAULT_PRECISION);
    }

    public LoadCommandArgs(String inputFile, String account, int dateField, int amountField, int descriptionField,
                           String dateFormat, char delimiter, boolean skipFirstLine, boolean dryRunOnly, int precision) {

        this.inputFile = inputFile;
        this.account = account;
        this.dateField = dateField;
        this.amountField = amountField;
        this.descriptionField = descriptionField;
        this.dateFormat = dateFormat;
        this.delimiter = delimiter;
        this.skipFirstLine = skipFirstLine;
        this.dryRunOnly = dryRunOnly;
        this.precision = precision;
    }

    @Override
    public void validate(Map<String, Object> parameters) throws ParameterException {
        // we can expect JCommander to have taken care of null and empty conditions
        String filePath = parameters.get("-f").toString();

        File file = new File(filePath);
        if (!file.exists()) {
            throw new ParameterException("'" + filePath + "' is not a valid file path");
        }

        if (!file.canRead()) {
            throw new ParameterException("Cannot read file: '" + filePath + "'");
        }
    }
}
