package org.todobot.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.parsers.command.task.PriorityParser;
import org.todobot.parsers.core.ParseResult;

public class PriorityParserTest {
    
    private PriorityParser priorityParser;
    
    private static final String VALID_PRIORITY_HIGH = "1 high";
    private static final String VALID_PRIORITY_MEDIUM = "2 medium";
    private static final String VALID_PRIORITY_LOW = "3 low";
    private static final String VALID_PRIORITY_H = "5 h";
    private static final String VALID_PRIORITY_M = "10 m";
    private static final String VALID_PRIORITY_L = "42 l";
    private static final String VALID_PRIORITY_WITH_EXCLAMATION = "1 !high";
    private static final String VALID_PRIORITY_WITH_SPACES = "  7   medium  ";
    
    private static final String INVALID_TASK_ZERO = "0 high";
    private static final String INVALID_TASK_NEGATIVE = "-1 medium";
    private static final String INVALID_TASK_DECIMAL = "1.5 low";
    private static final String INVALID_PRIORITY_LEVEL = "1 invalid";
    private static final String INVALID_MISSING_PRIORITY = "1";
    private static final String INVALID_MISSING_TASK = "high";
    private static final String INVALID_WRONG_ORDER = "high 1";
    private static final String EMPTY_STRING = "";
    
    @BeforeEach
    void setUp() {
        priorityParser = new PriorityParser();
    }
    
    // Valid priority formats
    @Test
    void shouldParseValidPriorityHigh() {
        ParseResult result = priorityParser.parse(VALID_PRIORITY_HIGH);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.PRIORITY, result.getCommandType());
        assertEquals("1", result.getArguments()[0]);
        assertEquals("high", result.getArguments()[1]);
    }
    
    @Test
    void shouldParseValidPriorityMedium() {
        ParseResult result = priorityParser.parse(VALID_PRIORITY_MEDIUM);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.PRIORITY, result.getCommandType());
        assertEquals("2", result.getArguments()[0]);
        assertEquals("medium", result.getArguments()[1]);
    }
    
    @Test
    void shouldParseValidPriorityLow() {
        ParseResult result = priorityParser.parse(VALID_PRIORITY_LOW);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.PRIORITY, result.getCommandType());
        assertEquals("3", result.getArguments()[0]);
        assertEquals("low", result.getArguments()[1]);
    }
    
    @Test
    void shouldParseShortFormPriorityH() {
        ParseResult result = priorityParser.parse(VALID_PRIORITY_H);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.PRIORITY, result.getCommandType());
        assertEquals("5", result.getArguments()[0]);
        assertEquals("h", result.getArguments()[1]);
    }
    
    @Test
    void shouldParseShortFormPriorityM() {
        ParseResult result = priorityParser.parse(VALID_PRIORITY_M);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.PRIORITY, result.getCommandType());
        assertEquals("10", result.getArguments()[0]);
        assertEquals("m", result.getArguments()[1]);
    }
    
    @Test
    void shouldParseShortFormPriorityL() {
        ParseResult result = priorityParser.parse(VALID_PRIORITY_L);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.PRIORITY, result.getCommandType());
        assertEquals("42", result.getArguments()[0]);
        assertEquals("l", result.getArguments()[1]);
    }
    
    @Test
    void shouldParsePriorityWithExclamationPrefix() {
        ParseResult result = priorityParser.parse(VALID_PRIORITY_WITH_EXCLAMATION);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.PRIORITY, result.getCommandType());
        assertEquals("1", result.getArguments()[0]);
        assertEquals("high", result.getArguments()[1]); // ! should be removed
    }
    
    @Test
    void shouldParsePriorityWithLeadingAndTrailingSpaces() {
        ParseResult result = priorityParser.parse(VALID_PRIORITY_WITH_SPACES);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.PRIORITY, result.getCommandType());
        assertEquals("7", result.getArguments()[0]);
        assertEquals("medium", result.getArguments()[1]);
    }
    
    // Invalid task number formats
    @Test
    void shouldRejectZeroTaskNumber() {
        ParseResult result = priorityParser.parse(INVALID_TASK_ZERO);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_PRIORITY_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectNegativeTaskNumber() {
        ParseResult result = priorityParser.parse(INVALID_TASK_NEGATIVE);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_PRIORITY_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDecimalTaskNumber() {
        ParseResult result = priorityParser.parse(INVALID_TASK_DECIMAL);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_PRIORITY_FORMAT, result.getErrorMessage());
    }
    
    // Invalid priority levels
    @Test
    void shouldRejectInvalidPriorityLevel() {
        ParseResult result = priorityParser.parse(INVALID_PRIORITY_LEVEL);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_PRIORITY_LEVEL, result.getErrorMessage());
    }
    
    // Invalid format cases
    @Test
    void shouldRejectMissingPriorityLevel() {
        ParseResult result = priorityParser.parse(INVALID_MISSING_PRIORITY);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_PRIORITY_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectMissingTaskNumber() {
        ParseResult result = priorityParser.parse(INVALID_MISSING_TASK);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_PRIORITY_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectWrongArgumentOrder() {
        ParseResult result = priorityParser.parse(INVALID_WRONG_ORDER);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_PRIORITY_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEmptyString() {
        ParseResult result = priorityParser.parse(EMPTY_STRING);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_PRIORITY_FORMAT, result.getErrorMessage());
    }
    
    // Test getCommandKeywords
    @Test
    void shouldReturnCorrectCommandKeywords() {
        String[] keywords = priorityParser.getCommandKeywords();
        
        assertEquals(1, keywords.length);
        assertEquals("priority", keywords[0]);
    }
}