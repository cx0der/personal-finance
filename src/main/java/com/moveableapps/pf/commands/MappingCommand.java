package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.Repository;

import java.io.PrintStream;

public class MappingCommand extends Command {

    private final Repository repository;
    private final MappingCommandArgs args;

    public MappingCommand(Repository repository, MappingCommandArgs mappingCommandArgs, PrintStream out, PrintStream err) {
        super(out, err);
        this.repository = repository;
        this.args = mappingCommandArgs;
    }

    @Override
    public int execute() {
        BookKeeper bookKeeper = new BookKeeper(repository);
        if (args.listMappings) {
            out.println("Txn Description\tMapped Account");
            bookKeeper.getAllAutoMappings().forEach((key, value) -> {
                String formatted = String.format("%s\t%s\n", key, value.accountId());
                out.print(formatted);
            });
        }
        return 0;
    }
}
