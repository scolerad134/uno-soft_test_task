package com.testtask.io;

import com.testtask.model.IntList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ResultWriter {
    void write(Path outputPath, List<IntList> groups, List<String> uniqueLines) throws IOException;
}
