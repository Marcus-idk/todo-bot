package org.todobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ParserTest {
    
    private static final String TEST_LIST_COMMAND = "list";
    private static final String TEST_BYE_COMMAND = "bye";
    private static final String TEST_MARK_COMMAND = "mark 5";
    private static final String TEST_UNMARK_COMMAND = "unmark 3";
    private static final String TEST_ADD_TASK = "todo read book";
    private static final String TEST_INVALID_FORMAT = "invalidcommand";
    private static final String TEST_EMPTY_INPUT = "";
    private static final String TEST_WHITESPACE_INPUT = "   ";
    
    @Test
    void shouldParseListCommand() {
        Parser parser = new Parser(TEST_LIST_COMMAND);
        assertTrue(parser.isListCommand());
        assertEquals("list", parser.getCommand());
        assertEquals("", parser.getArguments());
        assertEquals(TEST_LIST_COMMAND, parser.getOriginalInput());
    }
    
    @Test
    void shouldParseByeCommand() {
        Parser parser = new Parser(TEST_BYE_COMMAND);
        assertTrue(parser.isByeCommand());
        assertEquals("bye", parser.getCommand());
        assertEquals("", parser.getArguments());
        assertEquals(TEST_BYE_COMMAND, parser.getOriginalInput());
    }
    
    @Test
    void shouldParseMarkCommand() {
        Parser parser = new Parser(TEST_MARK_COMMAND);
        assertTrue(parser.isMarkCommand());
        assertEquals("mark", parser.getCommand());
        assertEquals("5", parser.getArguments());
        assertEquals(TEST_MARK_COMMAND, parser.getOriginalInput());
    }
    
    @Test
    void shouldParseUnmarkCommand() {
        Parser parser = new Parser(TEST_UNMARK_COMMAND);
        assertTrue(parser.isUnmarkCommand());
        assertEquals("unmark", parser.getCommand());
        assertEquals("3", parser.getArguments());
        assertEquals(TEST_UNMARK_COMMAND, parser.getOriginalInput());
    }
    
    @Test
    void shouldParseAddTaskCommand() {
        Parser parser = new Parser(TEST_ADD_TASK);
        assertTrue(parser.isAddTaskCommand());
        assertEquals("todo", parser.getCommand());
        assertEquals("read book", parser.getArguments());
        assertEquals(TEST_ADD_TASK, parser.getOriginalInput());
    }
    
    @Test
    void shouldDetectEmptyInput() {
        Parser parser = new Parser(TEST_EMPTY_INPUT);
        assertTrue(parser.isEmptyInput());
        assertEquals("", parser.getCommand());
        assertEquals("", parser.getArguments());
        assertEquals("", parser.getOriginalInput());
    }
    
    @Test
    void shouldDetectInvalidTaskFormat() {
        Parser parser = new Parser(TEST_INVALID_FORMAT);
        assertTrue(parser.isInvalidTaskFormat());
        assertFalse(parser.isAddTaskCommand());
        assertFalse(parser.hasSpacing());
    }
    
    @Test
    void shouldHandleCaseInsensitiveCommands() {
        Parser upperParser = new Parser("LIST");
        Parser mixedParser = new Parser("ByE");
        Parser lowerParser = new Parser("mark 1");
        
        assertTrue(upperParser.isListCommand());
        assertTrue(mixedParser.isByeCommand());
        assertTrue(lowerParser.isMarkCommand());
    }
    
    @Test
    void shouldExtractValidTaskNumber() {
        Parser parser = new Parser("mark 42");
        assertEquals(42, parser.getTaskNumber());
    }
    
    @Test
    void shouldReturnNegativeOneForInvalidTaskNumber() {
        Parser parser1 = new Parser("mark abc");
        Parser parser2 = new Parser("unmark !@#");
        Parser parser3 = new Parser("mark");
        
        assertEquals(-1, parser1.getTaskNumber());
        assertEquals(-1, parser2.getTaskNumber());
        assertEquals(-1, parser3.getTaskNumber());
    }
    
    @Test
    void shouldHandleZeroTaskNumber() {
        Parser parser = new Parser("mark 0");
        assertEquals(0, parser.getTaskNumber());
    }
    
    @Test
    void shouldHandleNegativeTaskNumber() {
        Parser parser = new Parser("unmark -5");
        assertEquals(-5, parser.getTaskNumber());
    }
    
    @Test
    void shouldHandleLargeTaskNumber() {
        Parser parser = new Parser("mark 999999");
        assertEquals(999999, parser.getTaskNumber());
    }
    
    @Test
    void shouldTrimWhitespaceInput() {
        Parser parser = new Parser("  list  ");
        assertTrue(parser.isListCommand());
        assertEquals("list", parser.getOriginalInput());
    }
    
    @Test
    void shouldHandleWhitespaceOnlyInput() {
        Parser parser = new Parser(TEST_WHITESPACE_INPUT);
        assertTrue(parser.isEmptyInput());
        assertEquals("", parser.getCommand());
        assertEquals("", parser.getArguments());
    }
    
    @Test
    void shouldDetectSpacingCorrectly() {
        Parser withSpacing = new Parser("todo read book");
        Parser withoutSpacing = new Parser("list");
        
        assertTrue(withSpacing.hasSpacing());
        assertFalse(withoutSpacing.hasSpacing());
    }
    
    @Test
    void shouldHandleMultipleSpacesInArguments() {
        Parser parser = new Parser("todo    read    multiple    books");
        assertEquals("todo", parser.getCommand());
        assertEquals("read    multiple    books", parser.getArguments());
        assertTrue(parser.isAddTaskCommand());
    }
    
    @Test
    void shouldHandleCommandWithExtraArguments() {
        Parser parser = new Parser("list extra arguments");
        assertTrue(parser.isListCommand());
        assertEquals("list", parser.getCommand());
        assertEquals("extra arguments", parser.getArguments());
    }
    
    @Test
    void shouldNotConfuseSimilarCommandNames() {
        Parser parser1 = new Parser("listing");
        Parser parser2 = new Parser("marker");
        Parser parser3 = new Parser("goodbye");
        
        assertFalse(parser1.isListCommand());
        assertFalse(parser2.isMarkCommand());
        assertFalse(parser3.isByeCommand());
        
        assertTrue(parser1.isInvalidTaskFormat());
        assertTrue(parser2.isInvalidTaskFormat());
        assertTrue(parser3.isInvalidTaskFormat());
    }

    @Test
    void shouldTreatInvalidCommandWithSpacingAsAddTask() {
        Parser parser = new Parser("marker 5");
        
        assertFalse(parser.isMarkCommand());
        assertTrue(parser.isAddTaskCommand());
        assertEquals("marker", parser.getCommand());
        assertEquals("5", parser.getArguments());
    }
    
    @Test
    void shouldHandleTaskNumberWithDecimal() {
        Parser parser = new Parser("mark 5.5");
        assertEquals(-1, parser.getTaskNumber());
    }
    
    @Test
    void shouldHandleTaskNumberWithLeadingZeros() {
        Parser parser = new Parser("unmark 007");
        assertEquals(7, parser.getTaskNumber());
    }
    
    @Test
    void shouldReturnCorrectBooleanCombinations() {
        // Test that only one command type returns true for any input
        Parser listParser = new Parser("list");
        assertTrue(listParser.isListCommand());
        assertFalse(listParser.isByeCommand());
        assertFalse(listParser.isMarkCommand());
        assertFalse(listParser.isUnmarkCommand());
        assertFalse(listParser.isAddTaskCommand());
        assertFalse(listParser.isInvalidTaskFormat());
        
        Parser addParser = new Parser("todo something");
        assertFalse(addParser.isListCommand());
        assertFalse(addParser.isByeCommand());
        assertFalse(addParser.isMarkCommand());
        assertFalse(addParser.isUnmarkCommand());
        assertTrue(addParser.isAddTaskCommand());
        assertFalse(addParser.isInvalidTaskFormat());
    }
}