package com.moveableapps.pf;

import picocli.CommandLine;

public class CommandFactory implements CommandLine.IFactory {
    @Override
    public <K> K create(Class<K> cls) throws Exception {
        return CommandLine.defaultFactory().create(cls);
    }
}
