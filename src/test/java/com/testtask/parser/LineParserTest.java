package com.testtask.parser;

import com.testtask.model.ParsedLine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LineParserTest {
    private final LineParser parser = new LineParser();

    @Test
    void parse_validLine_parsesAllColumns() {
        ParsedLine parsed = parser.parse("\"111\";\"\";\"abc\";\"42\"");
        assertNotNull(parsed);
        assertEquals(4, parsed.values().size());
        assertEquals("111", parsed.values().get(0));
        assertEquals("", parsed.values().get(1));
        assertEquals("abc", parsed.values().get(2));
        assertEquals("42", parsed.values().get(3));
    }

    @Test
    void parse_invalidLines_returnsNull() {
        assertNull(parser.parse("\"111\";\"222"));
        assertNull(parser.parse("111;\"222\""));
        assertNull(parser.parse("\"111\";"));
        assertNull(parser.parse("\"111\" \"222\""));
    }
}
