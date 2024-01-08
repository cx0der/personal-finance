package com.moveableapps.pf.commands;

import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingCommandTest {

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
    }

    @Test
    void listMappingsTest() {
        MappingCommandArgs args = new MappingCommandArgs(true);
        MappingCommand cmd = new MappingCommand(repository, args);

        int exitCode = cmd.execute();

        assertEquals(0, exitCode);
    }
}