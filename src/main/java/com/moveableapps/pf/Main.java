package com.moveableapps.pf;

import com.moveableapps.pf.commands.AccountCommand;
import com.moveableapps.pf.commands.LoadCommand;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.LoggerConfig;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;

import static org.apache.logging.log4j.LogManager.getLogger;

@Command(name = "pf", mixinStandardHelpOptions = true, version = "pf 1.0", description = "Personal finance", subcommands = {LoadCommand.class, AccountCommand.class})
public class Main implements Runnable {

    private static final Logger logger = getLogger(Main.class);

    @Option(names = {"--repo"}, defaultValue = "memory")
    String repo;

    @Option(names = {"-v", "--verbose"}, description = {"Specify multiple -v options to increase verbosity.", "For example, `-v -v -v` or `-vvv`"})
    boolean[] verbosity;

    private int executionStrategy(ParseResult parseResult) {
        init();
        return new CommandLine.RunLast().execute(parseResult);
    }

    private void init() {
        Level level = getLogLevel();
        LoggerContext loggerContext = LoggerContext.getContext(false);
        LoggerConfig rootConfig = loggerContext.getConfiguration().getRootLogger();
        for (Appender appender : rootConfig.getAppenders().values()) {
            if (appender instanceof ConsoleAppender) {
                rootConfig.removeAppender(appender.getName());
                rootConfig.addAppender(appender, level, null);
            }
        }
        if (rootConfig.getLevel().isMoreSpecificThan(level)) {
            rootConfig.setLevel(level);
        }
        loggerContext.updateLoggers(); // apply the changes
    }

    private Level getLogLevel() {
        if (verbosity == null) {
            return Level.WARN;
        }
        return switch (verbosity.length) {
            case 0 -> Level.WARN;
            case 1 -> Level.INFO;
            case 2 -> Level.DEBUG;
            default -> Level.TRACE;
        };
    }

    @Override
    public void run() {
        logger.info("Personal Finance v1.0");
        logger.info("Repo: " + repo);
    }

    public static void main(String[] args) {
        Main main = new Main();
        int exitCode = new CommandLine(main, new CommandFactory()).setExecutionStrategy(main::executionStrategy).execute(args);
        System.exit(exitCode);
    }
}
