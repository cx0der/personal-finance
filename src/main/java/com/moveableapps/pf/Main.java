package com.moveableapps.pf;

import com.moveableapps.pf.commands.AccountCommand;
import com.moveableapps.pf.commands.LoadCommand;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import static org.apache.logging.log4j.LogManager.getLogger;

@Command(name = "pf", mixinStandardHelpOptions = true, version = "pf 1.0", description = "Personal finance",
        subcommands = {LoadCommand.class, AccountCommand.class, CommandLine.HelpCommand.class})
public class Main implements Runnable {

    private static final Logger logger = getLogger(Main.class);

    @Option(names = {"--repo"}, defaultValue = "db")
    String repo;

    @Override
    public void run() {
        logger.info("Personal Finance v1.0");
        logger.info("Repo: " + repo);
    }

    public String getRepo() {
        return this.repo;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
