package com.moveableapps.pf.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.util.Map;

@Parameters(parametersValidators = MappingCommandArgs.class, commandDescription = "Add mapping")
public class MappingCommandArgs extends CommandArgs implements IParametersValidator {
    @Parameter(names = {"-l", "--list"}, description = "List all mappings")
    boolean listMappings;

    @Parameter(names = {"-a", "--add"}, description = "Add a mapping")
    boolean addMapping;

    @Parameter(names = {"-d", "--desc"}, description = "Transaction description")
    String txnDescription;

    @Parameter(names = {"-t", "--target"}, description = "Target account name")
    String targetAccount;

    public MappingCommandArgs() {
        // default constructor
    }

    public MappingCommandArgs(boolean listMappings) {
        this.listMappings = listMappings;
    }

    public MappingCommandArgs(String description, String targetAccount) {
        addMapping = true;
        txnDescription = description;
        this.targetAccount = targetAccount;
    }

    @Override
    public void validate(Map<String, Object> map) throws ParameterException {
        if (map.get("--list") == null && map.get("--add") == null) {
            throw new ParameterException("One of list or add options must be specified");
        }

        if (map.get("--add") != null) {
            // Add mode verify that description and account name is specified
            if (map.get("--desc") == null || map.get("--target") == null) {
                throw new ParameterException("Description and target account is required");
            }
        }
    }
}
