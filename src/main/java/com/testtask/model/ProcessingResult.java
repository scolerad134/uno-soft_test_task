package com.testtask.model;

import java.util.List;

public record ProcessingResult(
        List<IntList> groups,
        List<String> uniqueLines,
        int uniqueValidLines,
        int invalidLines
) {
}
