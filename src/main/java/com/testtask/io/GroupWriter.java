package com.testtask.io;

import com.testtask.model.IntList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GroupWriter implements ResultWriter {
    @Override
    public void write(Path outputPath, List<IntList> groups, List<String> uniqueLines) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write("Количество групп с более чем одним элементом: ");
            writer.write(Integer.toString(groups.size()));
            writer.newLine();
            writer.newLine();

            for (int i = 0; i < groups.size(); i++) {
                writer.write("Группа ");
                writer.write(Integer.toString(i + 1));
                writer.newLine();

                IntList group = groups.get(i);
                for (int j = 0; j < group.size(); j++) {
                    writer.write(uniqueLines.get(group.get(j)));
                    writer.newLine();
                }
                writer.newLine();
            }
        }
    }
}
