package com.moveableapps.pf;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.moveableapps.pf.commands.AddAccountCommand;
import com.moveableapps.pf.commands.AddAccountCommandArgs;
import com.moveableapps.pf.commands.Command;
import com.moveableapps.pf.commands.CommandArgs;
import com.moveableapps.pf.commands.ListAccountsArgs;
import com.moveableapps.pf.commands.ListAccountsCommand;
import com.moveableapps.pf.commands.LoadCommand;
import com.moveableapps.pf.commands.LoadCommandArgs;
import com.moveableapps.pf.commands.MappingCommand;
import com.moveableapps.pf.commands.MappingCommandArgs;
import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.DBRepository;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class Main {
    @Parameter(names = {"-r", "--repo"}, description = "Repo to use", hidden = true)
    String repo = "db";

    Repository getRepository(String type) {
        return type.equalsIgnoreCase("db") ? new DBRepository(Utils.getDatabasePath()) : new MemoryRepository();
    }

    BookKeeper getBookKeeper(Repository repository) {
        return new BookKeeper(repository);
    }

    Map<String, CommandArgs> getSubCommands() {
        Map<String, CommandArgs> subCommands = new HashMap<>();
        subCommands.put("list-accounts", new ListAccountsArgs());
        subCommands.put("add-account", new AddAccountCommandArgs());
        subCommands.put("load", new LoadCommandArgs());
        subCommands.put("map", new MappingCommandArgs());
        return subCommands;
    }

    public static void main(String[] args) {
        Main main = new Main();
        Map<String, CommandArgs> subCommandsMap = main.getSubCommands();

        JCommander.Builder builder = JCommander.newBuilder()
                .programName("pf")
                .addObject(main);

        subCommandsMap.forEach((builder::addCommand));

        JCommander jc = builder.build();

        try {
            jc.parse(args);
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            jc.usage();
            System.exit(1);
        }
        String parsedCommand = jc.getParsedCommand();
        if (parsedCommand == null || parsedCommand.isEmpty()) {
            jc.usage();
            System.exit(1);
        }
        Repository repository = main.getRepository(main.repo);
        BookKeeper bookKeeper = main.getBookKeeper(repository);

        Command command = null;

        switch (parsedCommand) {
            case "list-accounts": {
                command = new ListAccountsCommand();
                break;
            }
            case "add-account": {
                command = new AddAccountCommand();
                break;
            }
            case "load": {
                command = new LoadCommand();
                break;
            }
            case "map": {
                command = new MappingCommand();
                break;
            }
            default:
                jc.usage();
                System.exit(1);
        }
        int exitCode = 127;
        CommandArgs commandArgs = subCommandsMap.get(parsedCommand);
        try {
            exitCode = command.execute(commandArgs, bookKeeper, System.out, System.err);
        } catch (Exception e) {
            System.exit(exitCode);
        }
        System.exit(exitCode);
    }
}
