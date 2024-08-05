package com.moveableapps.pf.commands;

import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoadCommandArgsTest {

    private LoadCommandArgs args;

    @BeforeEach
    void setUp() {
        args = new LoadCommandArgs();
    }

    @Test
    @DisplayName("Throws exception for invalid file path")
    void validateInvalidFile() {
        Map<String, Object> params = new HashMap<>();
        params.put("-f", "sdf");

        ParameterException e = assertThrows(ParameterException.class, () -> args.validate(params));

        assertEquals("'sdf' is not a valid file path", e.getMessage());
    }

    @Test
    @DisplayName("Throws exception for unreadable file")
    void validateFileReadability() {
        Map<String, Object> params = new HashMap<>();
        params.put("-f", "/etc/shadow");

        ParameterException e = assertThrows(ParameterException.class, () -> args.validate(params));

        assertEquals("Cannot read file: '/etc/shadow'", e.getMessage());
    }
}
