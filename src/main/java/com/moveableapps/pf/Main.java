package com.moveableapps.pf;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.moveableapps.pf.commands.AddAccountCommand;
import com.moveableapps.pf.commands.AddAccountCommandArgs;
import com.moveableapps.pf.commands.AddMappingCommand;
import com.moveableapps.pf.commands.AddMappingCommandArgs;
import com.moveableapps.pf.commands.Command;
import com.moveableapps.pf.commands.CommandArgs;
import com.moveableapps.pf.commands.ListAccountsArgs;
import com.moveableapps.pf.commands.ListAccountsCommand;
import com.moveableapps.pf.commands.ListMappingsCommand;
import com.moveableapps.pf.commands.ListMappingsCommandArgs;
import com.moveableapps.pf.commands.LoadCommand;
import com.moveableapps.pf.commands.LoadCommandArgs;
import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.DBRepository;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.utils.Utils;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String APP_NAME = "pf";

    @Parameter(names = {"-r", "--repo"}, description = "Repo to use", hidden = true)
    private String repo = "db";

    private Repository getRepository(String type) {
        return type.equalsIgnoreCase("db") ? new DBRepository(Utils.getDatabasePath()) : new MemoryRepository();
    }

    private BookKeeper getBookKeeper(Repository repository) {
        return new BookKeeper(repository);
    }

    private Map<String, CommandArgs> getSubCommandArgsMap() {
        Map<String, CommandArgs> subCommands = new HashMap<>();
        subCommands.put(ListAccountsCommand.COMMAND_NAME, new ListAccountsArgs());
        subCommands.put(AddAccountCommand.COMMAND_NAME, new AddAccountCommandArgs());
        subCommands.put(LoadCommand.COMMAND_NAME, new LoadCommandArgs());
        subCommands.put(AddMappingCommand.COMMAND_NAME, new AddMappingCommandArgs());
        subCommands.put(ListMappingsCommand.COMMAND_NAME, new ListMappingsCommandArgs());
        return subCommands;
    }

    private Command getSubCommand(String commandName) {
        Command command = null;
        switch (commandName) {
            case ListAccountsCommand.COMMAND_NAME -> command = new ListAccountsCommand();
            case AddAccountCommand.COMMAND_NAME -> command = new AddAccountCommand();
            case LoadCommand.COMMAND_NAME -> command = new LoadCommand();
            case AddMappingCommand.COMMAND_NAME -> command = new AddMappingCommand();
            case ListMappingsCommand.COMMAND_NAME -> command = new ListMappingsCommand();
        }
        return command;
    }

    int run(String[] args, PrintStream out, PrintStream err) {
        Map<String, CommandArgs> subCommandArgsMap = getSubCommandArgsMap();

        JCommander jc = buildArgParser(subCommandArgsMap);

        try {
            jc.parse(args);
            // Get the sub-command
            String parsedCommand = jc.getParsedCommand();
            if (parsedCommand == null || parsedCommand.isEmpty()) {
                throw new IllegalArgumentException("Sub command is required.");
            }
            // Initialize the repository and Bookkeeper
            Repository repository = getRepository(repo);
            repository.initialize();
            BookKeeper bookKeeper = getBookKeeper(repository);

            // JCommand would have already checked if the sub-command is valid or not
            // so we are guaranteed to get a valid sub-command name
            Command subCommand = getSubCommand(parsedCommand);
            CommandArgs commandArgs = subCommandArgsMap.get(parsedCommand);

            // execute the sub-command
            return subCommand.execute(commandArgs, bookKeeper, out, err);
        } catch (ParameterException e) {
            err.println(e.getMessage());
            if (jc.getParsedCommand() != null) {
                jc.usage(jc.getParsedCommand());
            } else {
                jc.usage();
            }
            return 1;
        } catch (IllegalArgumentException e) {
            err.println(e.getMessage());
            jc.usage();
            return 2;
        } catch (Exception e) {
            err.println(e.getMessage());
            return 127;
        }
    }

    private JCommander buildArgParser(Map<String, CommandArgs> subCommandsMap) {
        JCommander.Builder builder = JCommander.newBuilder()
                .programName(APP_NAME)
                .addObject(this);

        subCommandsMap.forEach((builder::addCommand));

        return builder.build();
    }

    public static void main(String[] args) {
        Main main = new Main();
        int exitCode = main.run(args, System.out, System.err);
        System.exit(exitCode);
    }
}
