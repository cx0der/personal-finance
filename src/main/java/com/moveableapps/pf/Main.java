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
import com.moveableapps.pf.commands.ListMappingsCommand;
import com.moveableapps.pf.commands.ListMappingsCommandArgs;
import com.moveableapps.pf.commands.LoadCommand;
import com.moveableapps.pf.commands.LoadCommandArgs;
import com.moveableapps.pf.commands.AddMappingCommand;
import com.moveableapps.pf.commands.AddMappingCommandArgs;
import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.DBRepository;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.utils.Utils;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static final String APP_NAME = "pf";

    @Parameter(names = {"-r", "--repo"}, description = "Repo to use", hidden = true)
    String repo = "db";

    Repository getRepository(String type) {
        return type.equalsIgnoreCase("db") ? new DBRepository(Utils.getDatabasePath()) : new MemoryRepository();
    }

    BookKeeper getBookKeeper(Repository repository) {
        return new BookKeeper(repository);
    }

    Map<String, CommandArgs> getSubCommandsMap() {
        Map<String, CommandArgs> subCommands = new HashMap<>();
        subCommands.put(ListAccountsCommand.COMMAND_NAME, new ListAccountsArgs());
        subCommands.put(AddAccountCommand.COMMAND_NAME, new AddAccountCommandArgs());
        subCommands.put(LoadCommand.COMMAND_NAME, new LoadCommandArgs());
        subCommands.put(AddMappingCommand.COMMAND_NAME, new AddMappingCommandArgs());
        subCommands.put(ListMappingsCommand.COMMAND_NAME, new ListMappingsCommandArgs());
        return subCommands;
    }

    Optional<Command> getCommand(String commandName) {
        Command command;
        switch (commandName) {
            case ListAccountsCommand.COMMAND_NAME -> command = new ListAccountsCommand();
            case AddAccountCommand.COMMAND_NAME -> command = new AddAccountCommand();
            case LoadCommand.COMMAND_NAME -> command = new LoadCommand();
            case AddMappingCommand.COMMAND_NAME -> command = new AddMappingCommand();
            case ListMappingsCommand.COMMAND_NAME -> command = new ListMappingsCommand();
            default -> command = null;
        }
        return Optional.ofNullable(command);
    }

    int executeSubCommand(Command command, CommandArgs args, BookKeeper bookKeeper, PrintStream out, PrintStream err) {
        try {
            return command.execute(args, bookKeeper, out, err);
        } catch (Exception e) {
            err.println(e.getMessage());
            return 127;
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        Map<String, CommandArgs> subCommandsMap = main.getSubCommandsMap();

        JCommander.Builder builder = JCommander.newBuilder()
                .programName(APP_NAME)
                .addObject(main);

        subCommandsMap.forEach((builder::addCommand));

        JCommander jc = builder.build();

        try {
            jc.parse(args);
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            if (jc.getParsedCommand() != null) {
                jc.usage(jc.getParsedCommand());
            } else {
                jc.usage();
            }
            System.exit(1);
        }
        String parsedCommand = jc.getParsedCommand();
        if (parsedCommand == null || parsedCommand.isEmpty()) {
            jc.usage();
            System.exit(2);
        }
        Repository repository = main.getRepository(main.repo);
        BookKeeper bookKeeper = main.getBookKeeper(repository);

        main.getCommand(parsedCommand).ifPresentOrElse(command -> {
            CommandArgs commandArgs = subCommandsMap.get(parsedCommand);
            System.exit(main.executeSubCommand(command, commandArgs, bookKeeper, System.out, System.err));
        }, () -> {
            System.exit(3);
        });
    }
}
