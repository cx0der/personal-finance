package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.Repository;

public class MappingCommand implements Command {

    private final Repository repository;
    private final MappingCommandArgs args;

    public MappingCommand(Repository repository, MappingCommandArgs mappingCommandArgs) {
        this.repository = repository;
        this.args = mappingCommandArgs;
    }

    @Override
    public int execute() {
        BookKeeper bookKeeper = new BookKeeper(repository);
        if (args.listMappings) {
            System.out.println("Txn Description\tMapped Account\tMapping description");
            bookKeeper.getAllAutoMappings().forEach((key, value) -> {
                String formatted = String.format("%s\t%s\t%s\n", key, value.accountId(), value.description());
                System.out.println(formatted);
            });
        }
        return 0;
    }
}
