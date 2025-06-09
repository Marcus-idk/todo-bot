package org.todobot;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.todobot.parsers.ParseResult;
import org.todobot.parsers.Parser;

public class ParserTest {
    
    @Test
    void shouldParseListCommand() {
        ParseResult result = Parser.parse("list");
        assertTrue(result.isValid());
        assertEquals(CommandType.LIST, result.getCommandType());
        assertArrayEquals(new String[0], result.getArguments());
    }
    
    @Test
    void shouldParseByeCommand() {
        ParseResult result = Parser.parse("bye");
        assertTrue(result.isValid());
        assertEquals(CommandType.BYE, result.getCommandType());
        assertArrayEquals(new String[0], result.getArguments());
    }
    
    @Test
    void shouldParseMarkCommand() {
        ParseResult result = Parser.parse("mark 5");
        assertTrue(result.isValid());
        assertEquals(CommandType.MARK, result.getCommandType());
        assertArrayEquals(new String[]{"5"}, result.getArguments());
    }
    
    @Test
    void shouldParseUnmarkCommand() {
        ParseResult result = Parser.parse("unmark 3");
        assertTrue(result.isValid());
        assertEquals(CommandType.UNMARK, result.getCommandType());
        assertArrayEquals(new String[]{"3"}, result.getArguments());
    }
    
    @Test
    void shouldParseTodoCommand() {
        ParseResult result = Parser.parse("todo read book");
        assertTrue(result.isValid());
        assertEquals(CommandType.TODO, result.getCommandType());
        assertArrayEquals(new String[]{"read book"}, result.getArguments());
    }
    
    @Test
    void shouldParseDeadlineCommand() {
        ParseResult result = Parser.parse("deadline submit report /by Friday");
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertArrayEquals(new String[]{"submit report", "Friday"}, result.getArguments());
    }
    
    @Test
    void shouldParseEventCommand() {
        ParseResult result = Parser.parse("event meeting /from Mon 2pm /to 4pm");
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertArrayEquals(new String[]{"meeting", "Mon 2pm", "4pm"}, result.getArguments());
    }
    
    @Test
    void shouldDetectEmptyInput() {
        ParseResult result = Parser.parse("");
        assertFalse(result.isValid());
        assertEquals(BotMessages.EMPTY_INPUT, result.getErrorMessage());
    }
    
    @Test
    void shouldDetectWhitespaceOnlyInput() {
        ParseResult result = Parser.parse("   ");
        assertFalse(result.isValid());
        assertEquals(BotMessages.EMPTY_INPUT, result.getErrorMessage());
    }
    
    @Test
    void shouldDetectInvalidCommands() {
        ParseResult result1 = Parser.parse("invalidcommand");
        assertFalse(result1.isValid());
        assertEquals(BotMessages.INVALID_COMMAND, result1.getErrorMessage());
        
        ParseResult result2 = Parser.parse("xyz");
        assertFalse(result2.isValid());
        assertEquals(BotMessages.INVALID_COMMAND, result2.getErrorMessage());
    }
    
    @Test
    void shouldHandleCaseInsensitiveCommands() {
        ParseResult result1 = Parser.parse("LIST");
        assertTrue(result1.isValid());
        assertEquals(CommandType.LIST, result1.getCommandType());
        
        ParseResult result2 = Parser.parse("ByE");
        assertTrue(result2.isValid());
        assertEquals(CommandType.BYE, result2.getCommandType());
        
        ParseResult result3 = Parser.parse("TODO something");
        assertTrue(result3.isValid());
        assertEquals(CommandType.TODO, result3.getCommandType());
    }
    
    @Test
    void shouldRejectInvalidTaskNumbers() {
        ParseResult result1 = Parser.parse("mark abc");
        assertFalse(result1.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result1.getErrorMessage());
        
        ParseResult result2 = Parser.parse("unmark !@#");
        assertFalse(result2.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result2.getErrorMessage());
    }
    
    @Test
    void shouldRejectEmptyTodoDescription() {
        ParseResult result = Parser.parse("todo");
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_TODO_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectInvalidDeadlineFormat() {
        ParseResult result1 = Parser.parse("deadline submit report");
        assertFalse(result1.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result1.getErrorMessage());
        
        ParseResult result2 = Parser.parse("deadline /by Friday");
        assertFalse(result2.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result2.getErrorMessage());
        
        ParseResult result3 = Parser.parse("deadline submit report /by");
        assertFalse(result3.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result3.getErrorMessage());
    }
    
    @Test
    void shouldRejectInvalidEventFormat() {
        ParseResult result1 = Parser.parse("event meeting");
        assertFalse(result1.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result1.getErrorMessage());
        
        ParseResult result2 = Parser.parse("event meeting /from Mon");
        assertFalse(result2.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result2.getErrorMessage());
        
        ParseResult result3 = Parser.parse("event /from Mon /to 4pm");
        assertFalse(result3.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result3.getErrorMessage());
    }
    
    @Test
    void shouldTrimWhitespaceInput() {
        ParseResult result = Parser.parse("  list  ");
        assertTrue(result.isValid());
        assertEquals(CommandType.LIST, result.getCommandType());
    }
    
    @Test
    void shouldHandleMultipleSpacesInArguments() {
        ParseResult result = Parser.parse("todo    read    multiple    books");
        assertTrue(result.isValid());
        assertEquals(CommandType.TODO, result.getCommandType());
        assertArrayEquals(new String[]{"read    multiple    books"}, result.getArguments());
    }
    
    @Test
    void shouldHandleComplexDeadlineParsing() {
        ParseResult result = Parser.parse("deadline finish assignment with multiple words /by next Tuesday evening");
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertArrayEquals(new String[]{"finish assignment with multiple words", "next Tuesday evening"}, result.getArguments());
    }
    
    @Test
    void shouldHandleComplexEventParsing() {
        ParseResult result = Parser.parse("event team project discussion meeting /from Monday 2pm /to Wednesday 5pm");
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertArrayEquals(new String[]{"team project discussion meeting", "Monday 2pm", "Wednesday 5pm"}, result.getArguments());
    }
    
    @Test
    void shouldHandleNullInput() {
        ParseResult result = Parser.parse(null);
        assertFalse(result.isValid());
        assertEquals(BotMessages.EMPTY_INPUT, result.getErrorMessage());
    }
    
    @Test
    void shouldParseByeCommandVariants() {
        // Test all bye command variants map to canonical "bye" command
        ParseResult result1 = Parser.parse("exit");
        assertTrue(result1.isValid());
        assertEquals(CommandType.BYE, result1.getCommandType());
        assertArrayEquals(new String[0], result1.getArguments());
        
        ParseResult result2 = Parser.parse("quit");
        assertTrue(result2.isValid());
        assertEquals(CommandType.BYE, result2.getCommandType());
        assertArrayEquals(new String[0], result2.getArguments());
        
        ParseResult result3 = Parser.parse("goodbye");
        assertTrue(result3.isValid());
        assertEquals(CommandType.BYE, result3.getCommandType());
        assertArrayEquals(new String[0], result3.getArguments());
        
        ParseResult result4 = Parser.parse("done");
        assertTrue(result4.isValid());
        assertEquals(CommandType.BYE, result4.getCommandType());
        assertArrayEquals(new String[0], result4.getArguments());
    }
    
    @Test
    void shouldParseListCommandVariants() {
        // Test all list command variants map to canonical "list" command
        ParseResult result1 = Parser.parse("ls");
        assertTrue(result1.isValid());
        assertEquals(CommandType.LIST, result1.getCommandType());
        assertArrayEquals(new String[0], result1.getArguments());
        
        ParseResult result2 = Parser.parse("show");
        assertTrue(result2.isValid());
        assertEquals(CommandType.LIST, result2.getCommandType());
        assertArrayEquals(new String[0], result2.getArguments());
        
        ParseResult result3 = Parser.parse("display");
        assertTrue(result3.isValid());
        assertEquals(CommandType.LIST, result3.getCommandType());
        assertArrayEquals(new String[0], result3.getArguments());
    }
    
    @Test
    void shouldHandleCaseInsensitiveCommandVariants() {
        // Test case insensitive handling for new command variants
        ParseResult result1 = Parser.parse("EXIT");
        assertTrue(result1.isValid());
        assertEquals(CommandType.BYE, result1.getCommandType());
        
        ParseResult result2 = Parser.parse("QUIT");
        assertTrue(result2.isValid());
        assertEquals(CommandType.BYE, result2.getCommandType());
        
        ParseResult result3 = Parser.parse("LS");
        assertTrue(result3.isValid());
        assertEquals(CommandType.LIST, result3.getCommandType());
        
        ParseResult result4 = Parser.parse("SHOW");
        assertTrue(result4.isValid());
        assertEquals(CommandType.LIST, result4.getCommandType());
        
        ParseResult result5 = Parser.parse("DiSpLaY");
        assertTrue(result5.isValid());
        assertEquals(CommandType.LIST, result5.getCommandType());
    }
    
    @Test
    void shouldTrimWhitespaceForCommandVariants() {
        // Test whitespace handling for new command variants
        ParseResult result1 = Parser.parse("  exit  ");
        assertTrue(result1.isValid());
        assertEquals(CommandType.BYE, result1.getCommandType());
        
        ParseResult result2 = Parser.parse("  ls  ");
        assertTrue(result2.isValid());
        assertEquals(CommandType.LIST, result2.getCommandType());
    }
}