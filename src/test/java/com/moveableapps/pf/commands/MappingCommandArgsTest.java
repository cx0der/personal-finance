package com.moveableapps.pf.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MappingCommandArgsTest {

    @Test
    void should_throwException_whenNoOptions() {
        String[] args = {"mapping"};
        JCommander jc = buildCommander();

        assertThrows(ParameterException.class, () -> jc.parse(args));
    }

    @Test
    void should_passValidation_whenListOptionIsSet() {
        String[] args = {"mapping", "-l"};
        MappingCommandArgs cmdArgs = new MappingCommandArgs();
        JCommander jc = buildCommanderWith(cmdArgs);
        jc.parse(args);

        assertTrue(cmdArgs.listMappings);
    }

    @Test
    void should_throwException_whenAddParamsAreMissing() {
        String[] args = {"mapping", "-a"};
        JCommander jc = buildCommander();

        assertThrows(ParameterException.class, () -> jc.parse(args));
    }

    @Test
    void should_passValidation_whenAllAddOptionsAreGiven() {
        String[] args = {"mapping", "--add", "-d", "groceries", "--target", "Expenses:Groceries"};
        MappingCommandArgs cmdArgs = new MappingCommandArgs();
        JCommander jc = buildCommanderWith(cmdArgs);
        jc.parse(args);

        assertTrue(cmdArgs.addMapping);
    }

    private JCommander buildCommander() {
        return buildCommanderWith(new MappingCommandArgs());
    }

    private JCommander buildCommanderWith(MappingCommandArgs cmdArgs) {
        return JCommander.newBuilder().addCommand("mapping", cmdArgs).build();
    }
}
