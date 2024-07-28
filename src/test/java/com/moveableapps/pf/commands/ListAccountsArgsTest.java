package com.moveableapps.pf.commands;

import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListAccountsArgsTest {
    @Test
    @DisplayName("Validate handles empty params")
    void validateEmpty() {
        ListAccountsArgs args = new ListAccountsArgs();
        assertDoesNotThrow(() -> args.validate(new HashMap<>()));
    }

    @Test
    @DisplayName("Validate handles pattern correctly")
    void validatePattern() {
        ListAccountsArgs args = new ListAccountsArgs();
        Map<String, Object> params = new HashMap<>();
        params.put("--pattern", "test");
        assertDoesNotThrow(() -> args.validate(params));
    }

    @Test
    @DisplayName("Validate handles null pattern correctly")
    void validateNullPattern() {
        ListAccountsArgs args = new ListAccountsArgs();
        Map<String, Object> params = new HashMap<>();
        params.put("--pattern", null);
        assertDoesNotThrow(() -> args.validate(params));
    }


    @Test
    @DisplayName("Validate throws exception for invalid pattern")
    void validateInvalidPattern() {
        Map<String, Object> params = new HashMap<>();
        params.put("--pattern", "*d");
        ListAccountsArgs args = new ListAccountsArgs();
        assertThrows(ParameterException.class, () -> args.validate(params));
    }
}
