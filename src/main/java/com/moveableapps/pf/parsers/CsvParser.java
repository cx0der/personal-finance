package com.moveableapps.pf.parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvParser {

    private static final char DOUBLE_QUOTES = '"';

    private final String fileName;
    private final char delimiter;

    public CsvParser(String fileName, char delimiter) {
        this.fileName = fileName;
        this.delimiter = delimiter;
    }

    public List<Map<Integer, String>> parse(boolean skipFirstLine, int[] fieldsToSelect) {
        List<Map<Integer, String>> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineCount = 1;
            while ((line = reader.readLine()) != null) {
                if (skipFirstLine && lineCount == 1) {
                    lineCount++;
                    continue;
                }
                Map<Integer, String> fields = parseLine(line, fieldsToSelect);
                lineCount++;
                result.add(fields);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Map<Integer, String> parseLine(String line, int[] fieldsToSelect) {
        Map<Integer, String> fields = new HashMap<>();

        boolean inQuotes = false;
        boolean isFieldWithDoubleQuotes = false;

        StringBuilder field = new StringBuilder();
        int fieldCount = 1;

        for (char c : line.toCharArray()) {
            if (c == DOUBLE_QUOTES) {
                if (isFieldWithDoubleQuotes) {
                    if (!field.isEmpty()) {
                        field.append(DOUBLE_QUOTES);
                        isFieldWithDoubleQuotes = false;
                    }
                } else {
                    isFieldWithDoubleQuotes = true;
                }
            } else {
                isFieldWithDoubleQuotes = false;
            }

            if (c == DOUBLE_QUOTES) {
                inQuotes = !inQuotes;
            } else {
                if (c == delimiter && !inQuotes) {
                    if (shouldPickField(fieldsToSelect, fieldCount)) {
                        fields.put(fieldCount, field.toString());
                    }
                    fieldCount++;
                    field.setLength(0);
                } else {
                    field.append(c);
                }
            }
        }
        if (shouldPickField(fieldsToSelect, fieldCount)) {
            fields.put(fieldCount, field.toString());
        }
        return fields;
    }

    private boolean shouldPickField(int[] fieldsToSelect, int currentField) {
        return Arrays.stream(fieldsToSelect).anyMatch(i -> i == currentField);
    }
}
