package com.testtask.app;

import com.testtask.io.GroupWriter;
import com.testtask.io.ResultWriter;
import com.testtask.model.ProcessingResult;
import com.testtask.parser.LineParser;
import com.testtask.service.GroupingService;

import java.nio.file.Files;
import java.nio.file.Path;

public class Application {
    private final GroupingService groupingService;
    private final ResultWriter resultWriter;

    public Application() {
        this(new GroupingService(new LineParser()), new GroupWriter());
    }

    public Application(GroupingService groupingService, ResultWriter resultWriter) {
        this.groupingService = groupingService;
        this.resultWriter = resultWriter;
    }

    public void run(String[] args) throws Exception {
        long start = System.nanoTime();

        if (args.length < 1) {
            System.err.println("Usage: java -jar uno-soft-test-task.jar <input-file> [output-file]");
            System.exit(1);
        }

        Path inputPath = Path.of(args[0]).toAbsolutePath().normalize();
        Path outputPath = args.length > 1
                ? Path.of(args[1]).toAbsolutePath().normalize()
                : Path.of("output.txt").toAbsolutePath().normalize();

        if (!Files.exists(inputPath) || !Files.isRegularFile(inputPath)) {
            System.err.println("Input file not found: " + inputPath);
            System.exit(1);
        }

        ProcessingResult result = groupingService.process(inputPath);
        resultWriter.write(outputPath, result.groups(), result.uniqueLines());

        long elapsedMs = (System.nanoTime() - start) / 1_000_000L;
        System.out.println("Groups with size > 1: " + result.groups().size());
        System.out.println("Execution time (ms): " + elapsedMs);
        System.out.println("Output file: " + outputPath);
        System.out.println("Unique valid lines: " + result.uniqueValidLines());
        System.out.println("Skipped invalid lines: " + result.invalidLines());
    }
}
