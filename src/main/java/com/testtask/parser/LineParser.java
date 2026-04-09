package com.testtask.parser;

import com.testtask.model.ParsedLine;

public class LineParser implements Parser<ParsedLine> {
    @Override
    public ParsedLine parse(String line) {
        ParsedLine parsed = new ParsedLine();
        int i = 0;
        int n = line.length();

        while (i < n) {
            if (line.charAt(i) != '"') {
                return null;
            }
            i++;

            int start = i;
            while (i < n && line.charAt(i) != '"') {
                i++;
            }
            if (i >= n) {
                return null;
            }

            parsed.values().add(line.substring(start, i));
            i++;

            if (i == n) {
                break;
            }
            if (line.charAt(i) != ';') {
                return null;
            }
            i++;
            if (i == n) {
                return null;
            }
        }

        return parsed;
    }
}
