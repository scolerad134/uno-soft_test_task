package com.testtask.parser;

import com.testtask.model.ParsedLine;

/**
 * Parses lines from the task datasets: semicolon-separated fields.
 * A field is either a quoted string {@code "..."} (empty allowed: {@code ""})
 * or an empty field represented by consecutive semicolons {@code ;;} without quotes
 * (as in {@code lng-big.csv}).
 */
public class LineParser implements Parser<ParsedLine> {
    @Override
    public ParsedLine parse(String line) {
        ParsedLine parsed = new ParsedLine();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ';' && !inQuotes) {
                parsed.values().add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }

        if (inQuotes) {
            return null;
        }

        parsed.values().add(field.toString());
        return parsed;
    }
}
