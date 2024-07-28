package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.AutoMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListMappingsCommandTest {

    private Repository repository;
    private BookKeeper bookKeeper;
    private ListMappingsCommand command;

    @BeforeEach
    void setup() {
        repository = new MemoryRepository();
        bookKeeper = new BookKeeper(repository);
        command = new ListMappingsCommand();
    }

    private String getFirstLine() {
        return "Description\tAccount id\n";
    }

    private String getMappingLine(AutoMapping mapping) {
        return String.format("%s\t%d\n", mapping.description(), mapping.accountId());
    }

    @Test
    @DisplayName("Lists all mappings")
    void listsAllMappings() throws Exception {
        AutoMapping mapping = new AutoMapping("groceries", 1);
        repository.addAutoMapping(mapping);

        ListMappingsCommandArgs args = new ListMappingsCommandArgs();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        command.execute(args, bookKeeper, new PrintStream(stream), System.err);

        assertEquals(getFirstLine() + getMappingLine(mapping), stream.toString());
    }
}
