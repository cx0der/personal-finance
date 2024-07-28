package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.entities.AutoMapping;

import java.io.PrintStream;
import java.util.Map;

public class ListMappingsCommand implements Command {
    public static final String COMMAND_NAME = "list-mappings";

    @Override
    public int execute(CommandArgs commandArgs, BookKeeper bookKeeper, PrintStream out, PrintStream err) throws Exception {
        Map<String, AutoMapping> mappings = bookKeeper.getAllAutoMappings();
        printMappings(mappings, out);
        return 0;
    }

    private void printMappings(Map<String, AutoMapping> mappings, PrintStream out) {
        out.println("Description\tAccount id");
        mappings.forEach(((s, mapping) -> out.println(getMappingLine(mapping))));
    }

    private String getMappingLine(AutoMapping mapping) {
        return String.format("%s\t%d", mapping.description(), mapping.accountId());
    }
}
