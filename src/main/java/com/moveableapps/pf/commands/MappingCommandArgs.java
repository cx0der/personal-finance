package com.moveableapps.pf.commands;

import com.beust.jcommander.Parameter;

public class MappingCommandArgs {
    @Parameter(names = {"-l", "--list"}, description = "List all mappings")
    boolean listMappings;

    public MappingCommandArgs() {
        // default constructor
    }

    public MappingCommandArgs(boolean listMappings) {
        this.listMappings = listMappings;
    }
}
