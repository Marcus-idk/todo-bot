package org.todobot;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ParserTest {
    
    @Test
    void shouldParseListCommand() {
        Parser parser = new Parser("list");
        assertTrue(parser.isListCommand());
        assertEquals("list", parser.getCommand());
        assertEquals("", parser.getArguments());
    }
    
    @Test
    void shouldParseByeCommand() {
        Parser parser = new Parser("bye");
        assertTrue(parser.isByeCommand());
        assertEquals("bye", parser.getCommand());
        assertEquals("", parser.getArguments());
    }
    
    @Test
    void shouldParseMarkCommand() {
        Parser parser = new Parser("mark 5");
        assertTrue(parser.isMarkCommand());
        assertEquals("mark", parser.getCommand());
        assertEquals("5", parser.getArguments());
    }
    
    @Test
    void shouldParseUnmarkCommand() {
        Parser parser = new Parser("unmark 3");
        assertTrue(parser.isUnmarkCommand());
        assertEquals("unmark", parser.getCommand());
        assertEquals("3", parser.getArguments());
    }
    
    @Test
    void shouldParseTodoCommand() {
        Parser parser = new Parser("todo read book");
        assertTrue(parser.isTodoCommand());
        assertEquals("todo", parser.getCommand());
        assertEquals("read book", parser.getArguments());
    }
    
    @Test
    void shouldParseDeadlineCommand() {
        Parser parser = new Parser("deadline submit report /by Friday");
        assertTrue(parser.isDeadlineCommand());
        assertEquals("deadline", parser.getCommand());
        assertEquals("submit report /by Friday", parser.getArguments());
    }
    
    @Test
    void shouldParseEventCommand() {
        Parser parser = new Parser("event meeting /from Mon 2pm /to 4pm");
        assertTrue(parser.isEventCommand());
        assertEquals("event", parser.getCommand());
        assertEquals("meeting /from Mon 2pm /to 4pm", parser.getArguments());
    }
    
    @Test
    void shouldDetectEmptyInput() {
        Parser parser = new Parser("");
        assertTrue(parser.isEmptyInput());
        assertEquals("", parser.getCommand());
        assertEquals("", parser.getArguments());
    }
    
    @Test
    void shouldDetectValidCommands() {
        assertTrue(new Parser("list").isValidCommand());
        assertTrue(new Parser("bye").isValidCommand());
        assertTrue(new Parser("mark 1").isValidCommand());
        assertTrue(new Parser("unmark 1").isValidCommand());
        assertTrue(new Parser("todo something").isValidCommand());
        assertTrue(new Parser("deadline task /by date").isValidCommand());
        assertTrue(new Parser("event task /from start /to end").isValidCommand());
    }
    
    @Test
    void shouldDetectInvalidCommands() {
        assertTrue(new Parser("invalidcommand").isInvalidCommand());
        assertTrue(new Parser("xyz").isInvalidCommand());
        assertFalse(new Parser("list").isInvalidCommand());
        assertFalse(new Parser("").isInvalidCommand());
    }
    
    @Test
    void shouldHandleCaseInsensitiveCommands() {
        assertTrue(new Parser("LIST").isListCommand());
        assertTrue(new Parser("ByE").isByeCommand());
        assertTrue(new Parser("MARK 1").isMarkCommand());
        assertTrue(new Parser("TODO something").isTodoCommand());
    }
    
    @Test
    void shouldExtractValidTaskNumber() {
        Parser parser = new Parser("mark 42");
        assertEquals(42, parser.getTaskNumber());
    }
    
    @Test
    void shouldReturnNegativeOneForInvalidTaskNumber() {
        assertEquals(-1, new Parser("mark abc").getTaskNumber());
        assertEquals(-1, new Parser("unmark !@#").getTaskNumber());
        assertEquals(-1, new Parser("mark").getTaskNumber());
    }
    
    @Test
    void shouldGetTodoDescription() {
        Parser parser = new Parser("todo read a book");
        assertEquals("read a book", parser.getTodoDescription());
    }
    
    @Test
    void shouldParseValidDeadline() {
        Parser parser = new Parser("deadline submit report /by Friday 5pm");
        String[] result = parser.parseDeadline();
        assertArrayEquals(new String[]{"submit report", "Friday 5pm"}, result);
    }
    
    @Test
    void shouldReturnNullForInvalidDeadlineFormat() {
        assertNull(new Parser("deadline submit report").parseDeadline());
        assertNull(new Parser("deadline /by Friday").parseDeadline());
        assertNull(new Parser("deadline submit report /by").parseDeadline());
    }
    
    @Test
    void shouldParseValidEvent() {
        Parser parser = new Parser("event project meeting /from Mon 2pm /to 4pm");
        String[] result = parser.parseEvent();
        assertArrayEquals(new String[]{"project meeting", "Mon 2pm", "4pm"}, result);
    }
    
    @Test
    void shouldReturnNullForInvalidEventFormat() {
        assertNull(new Parser("event meeting").parseEvent());
        assertNull(new Parser("event meeting /from Mon").parseEvent());
        assertNull(new Parser("event meeting /to 4pm").parseEvent());
        assertNull(new Parser("event /from Mon /to 4pm").parseEvent());
    }
    
    @Test
    void shouldTrimWhitespaceInput() {
        Parser parser = new Parser("  list  ");
        assertTrue(parser.isListCommand());
        assertEquals("list", parser.getOriginalInput());
    }
    
    @Test
    void shouldHandleWhitespaceOnlyInput() {
        Parser parser = new Parser("   ");
        assertTrue(parser.isEmptyInput());
        assertEquals("", parser.getCommand());
        assertEquals("", parser.getArguments());
    }
    
    @Test
    void shouldHandleMultipleSpacesInArguments() {
        Parser parser = new Parser("todo    read    multiple    books");
        assertEquals("todo", parser.getCommand());
        assertEquals("read    multiple    books", parser.getArguments());
        assertTrue(parser.isTodoCommand());
    }
    
    @Test
    void shouldHandleComplexDeadlineParsing() {
        Parser parser = new Parser("deadline finish assignment with multiple words /by next Tuesday evening");
        String[] result = parser.parseDeadline();
        assertArrayEquals(new String[]{"finish assignment with multiple words", "next Tuesday evening"}, result);
    }
    
    @Test
    void shouldHandleComplexEventParsing() {
        Parser parser = new Parser("event team project discussion meeting /from Monday 2pm /to Wednesday 5pm");
        String[] result = parser.parseEvent();
        assertArrayEquals(new String[]{"team project discussion meeting", "Monday 2pm", "Wednesday 5pm"}, result);
    }
}