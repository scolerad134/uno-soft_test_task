package com.testtask.service;

import com.testtask.model.IntList;
import com.testtask.model.ProcessingResult;
import com.testtask.parser.LineParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupingServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void process_groupsByEqualValuesInSameColumnWithTransitivity() throws IOException {
        Path input = tempDir.resolve("input.txt");
        List<String> lines = List.of(
                "\"111\";\"123\";\"222\"",
                "\"200\";\"123\";\"100\"",
                "\"300\";\"\";\"100\""
        );
        Files.write(input, lines, StandardCharsets.UTF_8);

        GroupingService service = new GroupingService(new LineParser());
        ProcessingResult result = service.process(input);

        assertEquals(1, result.groups().size());
        assertEquals(3, result.groups().get(0).size());
        assertEquals(3, result.uniqueValidLines());
        assertEquals(0, result.invalidLines());
    }

    @Test
    void process_doesNotGroupWhenMatchingValueIsInDifferentColumns() throws IOException {
        Path input = tempDir.resolve("input.txt");
        List<String> lines = List.of(
                "\"100\";\"200\";\"300\"",
                "\"200\";\"300\";\"100\""
        );
        Files.write(input, lines, StandardCharsets.UTF_8);

        GroupingService service = new GroupingService(new LineParser());
        ProcessingResult result = service.process(input);

        assertEquals(0, result.groups().size());
        assertEquals(2, result.uniqueValidLines());
        assertEquals(0, result.invalidLines());
    }

    @Test
    void process_skipsInvalidAndDuplicateLines() throws IOException {
        Path input = tempDir.resolve("input.txt");
        List<String> lines = List.of(
                "\"A\";\"B\"",
                "\"A\";\"B\"",
                "\"C\";\"B\"",
                "\"broken\"line\"",
                "\"D\";"
        );
        Files.write(input, lines, StandardCharsets.UTF_8);

        GroupingService service = new GroupingService(new LineParser());
        ProcessingResult result = service.process(input);

        assertEquals(1, result.groups().size());
        assertEquals(2, result.groups().get(0).size());
        assertEquals(2, result.uniqueValidLines());
        assertEquals(2, result.invalidLines());
    }

    @Test
    void process_sortsGroupsBySizeDescending() throws IOException {
        Path input = tempDir.resolve("input.txt");
        List<String> lines = List.of(
                "\"A\";\"x\"",
                "\"B\";\"x\"",
                "\"C\";\"x\"",
                "\"D\";\"y\"",
                "\"E\";\"y\""
        );
        Files.write(input, lines, StandardCharsets.UTF_8);

        GroupingService service = new GroupingService(new LineParser());
        ProcessingResult result = service.process(input);

        assertEquals(2, result.groups().size());
        assertEquals(3, result.groups().get(0).size());
        assertEquals(2, result.groups().get(1).size());

        Set<String> topGroupLines = extractGroupLines(result.groups().get(0), result.uniqueLines());
        assertEquals(Set.of("\"A\";\"x\"", "\"B\";\"x\"", "\"C\";\"x\""), topGroupLines);
    }

    private Set<String> extractGroupLines(IntList group, List<String> allLines) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            lines.add(allLines.get(group.get(i)));
        }
        return new HashSet<>(lines);
    }
}
