package com.moveableapps.pf.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Parameters(parametersValidators = ListAccountsArgs.class, commandDescription = "List accounts")
public class ListAccountsArgs extends CommandArgs implements IParametersValidator {

    @Parameter(names = {"-p", "--pattern"}, description = "Regex pattern")
    private String pattern;

    public ListAccountsArgs() {
        // Default constructor
    }

    public ListAccountsArgs(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return this.pattern;
    }

    @Override
    public void validate(Map<String, Object> parameters) throws ParameterException {
        if (parameters.containsKey("--pattern") && parameters.get("--pattern") != null) {
            String patternString = parameters.get("--pattern").toString();

            try {
                Pattern.compile(patternString);
            } catch (PatternSyntaxException e) {
                throw new ParameterException(e.getMessage());
            }
        }
    }
}
