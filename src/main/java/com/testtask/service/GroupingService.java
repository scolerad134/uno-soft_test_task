package com.testtask.service;

import com.testtask.dsu.DisjointSetUnion;
import com.testtask.model.IntList;
import com.testtask.model.ParsedLine;
import com.testtask.model.ProcessingResult;
import com.testtask.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupingService {
    private final Parser<ParsedLine> lineParser;

    public GroupingService(Parser<ParsedLine> lineParser) {
        this.lineParser = lineParser;
    }

    public ProcessingResult process(Path inputPath) throws IOException {
        Map<String, Integer> uniqueLineToId = new HashMap<>(1 << 20);
        List<String> uniqueLines = new ArrayList<>(1 << 20);
        List<Map<String, Integer>> columnValueOwner = new ArrayList<>();
        DisjointSetUnion dsu = new DisjointSetUnion();
        int invalidLines = 0;

        try (BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                ParsedLine parsed = lineParser.parse(line);
                if (parsed == null) {
                    invalidLines++;
                    continue;
                }

                Integer existingId = uniqueLineToId.get(line);
                if (existingId != null) {
                    continue;
                }

                int id = dsu.addNode();
                uniqueLineToId.put(line, id);
                uniqueLines.add(line);

                for (int col = 0; col < parsed.values().size(); col++) {
                    String value = parsed.values().get(col);
                    if (value.isEmpty()) {
                        continue;
                    }

                    while (columnValueOwner.size() <= col) {
                        columnValueOwner.add(new HashMap<>());
                    }

                    Map<String, Integer> owners = columnValueOwner.get(col);
                    Integer prev = owners.putIfAbsent(value, id);
                    if (prev != null) {
                        dsu.union(id, prev);
                    }
                }
            }
        }

        Map<Integer, Integer> rootToGroupIndex = new HashMap<>();
        List<IntList> groups = new ArrayList<>();
        for (int i = 0; i < uniqueLines.size(); i++) {
            int root = dsu.find(i);
            if (dsu.componentSize(root) <= 1) {
                continue;
            }
            Integer groupIndex = rootToGroupIndex.get(root);
            if (groupIndex == null) {
                groupIndex = groups.size();
                rootToGroupIndex.put(root, groupIndex);
                groups.add(new IntList());
            }
            groups.get(groupIndex).add(i);
        }

        groups.sort((a, b) -> Integer.compare(b.size(), a.size()));
        return new ProcessingResult(groups, uniqueLines, uniqueLines.size(), invalidLines);
    }
}
