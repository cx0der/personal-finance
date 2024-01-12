package com.moveableapps.pf;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.moveableapps.pf.commands.AccountCommand;
import com.moveableapps.pf.commands.AccountCommandArgs;
import com.moveableapps.pf.commands.Command;
import com.moveableapps.pf.commands.LoadCommand;
import com.moveableapps.pf.commands.LoadCommandArgs;
import com.moveableapps.pf.commands.MappingCommand;
import com.moveableapps.pf.commands.MappingCommandArgs;
import com.moveableapps.pf.data.DBRepository;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.utils.Utils;

public class Main {
    @Parameter(names = {"-r", "--repo"}, description = "Repo to use", hidden = true)
    String repo = "db";

    private Repository repository;

    void init() {
        repository = repo.equalsIgnoreCase("db") ? new DBRepository(Utils.getDatabasePath()) : new MemoryRepository();
    }

    public static void main(String[] args) {
        Main main = new Main();
        AccountCommandArgs accountCommandArgs = new AccountCommandArgs();
        LoadCommandArgs loadCommandArgs = new LoadCommandArgs();
        MappingCommandArgs mappingCommandArgs = new MappingCommandArgs();
        JCommander jc = JCommander.newBuilder()
                .programName("pf")
                .addObject(main)
                .addCommand("account", accountCommandArgs)
                .addCommand("load", loadCommandArgs)
                .addCommand("map", mappingCommandArgs)
                .build();

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
        main.init();

        Command command = null;

        switch (parsedCommand) {
            case "account": {
                command = new AccountCommand(main.repository, accountCommandArgs, System.out, System.err);
                break;
            }
            case "load": {
                command = new LoadCommand(main.repository, loadCommandArgs, System.out, System.err);
                break;
            }
            case "map": {
                command = new MappingCommand(main.repository, mappingCommandArgs, System.out, System.err);
                break;
            }
            default:
                jc.usage();
                System.exit(1);
        }
        int exitCode = 127;
        try {
            exitCode = command.execute();
        } catch (Exception e) {
            System.exit(exitCode);
        }
        System.exit(exitCode);
    }
}
