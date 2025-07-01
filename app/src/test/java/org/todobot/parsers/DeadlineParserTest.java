package org.todobot.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;

public class DeadlineParserTest {
    
    private DeadlineParser deadlineParser;
    
    private static final String VALID_DEADLINE_DATE = "homework /by 25-12-2023";
    private static final String VALID_DEADLINE_DATETIME = "buy milk /by 01-01-2024 14:30";
    private static final String VALID_DEADLINE_WITH_SPACES = "   task with spaces   /by   25-12-2023   ";
    private static final String VALID_COMPLEX_TASK = "finish the project report /by 31-12-2023 23:59";
    private static final String VALID_TASK_WITH_NUMBERS = "read book chapter 5 /by 15-06-2024";
    
    private static final String INVALID_MISSING_BY = "homework 25-12-2023";
    private static final String INVALID_MISSING_TASK = "/by 25-12-2023";
    private static final String INVALID_MISSING_DATE = "homework /by";
    private static final String INVALID_MISSING_SLASH = "homework by 25-12-2023";
    private static final String INVALID_DATE_FORMAT = "homework /by invalid-date";
    private static final String INVALID_EMPTY_TASK = " /by 25-12-2023";
    private static final String INVALID_EMPTY_DATE = "homework /by ";
    private static final String EMPTY_STRING = "";
    private static final String JUST_BY = "/by";
    
    @BeforeEach
    void setUp() {
        deadlineParser = new DeadlineParser();
    }
    
    // Valid deadline formats
    @Test
    void shouldParseValidDeadlineWithDate() {
        ParseResult result = deadlineParser.parse(VALID_DEADLINE_DATE);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals("homework", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        assertEquals(expectedDateTime, timeData[0]);
        assertEquals(false, timeData[1]); // hasTime = false
    }
    
    @Test
    void shouldParseValidDeadlineWithDateTime() {
        ParseResult result = deadlineParser.parse(VALID_DEADLINE_DATETIME);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals("buy milk", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedDateTime = LocalDateTime.of(2024, 1, 1, 14, 30);
        assertEquals(expectedDateTime, timeData[0]);
        assertEquals(true, timeData[1]); // hasTime = true
    }
    
    @Test
    void shouldParseDeadlineWithLeadingAndTrailingSpaces() {
        ParseResult result = deadlineParser.parse(VALID_DEADLINE_WITH_SPACES);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals("task with spaces", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        assertEquals(expectedDateTime, timeData[0]);
        assertEquals(false, timeData[1]);
    }
    
    @Test
    void shouldParseComplexTaskDescription() {
        ParseResult result = deadlineParser.parse(VALID_COMPLEX_TASK);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals("finish the project report", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 12, 31, 23, 59);
        assertEquals(expectedDateTime, timeData[0]);
        assertEquals(true, timeData[1]);
    }
    
    @Test
    void shouldParseTaskWithNumbers() {
        ParseResult result = deadlineParser.parse(VALID_TASK_WITH_NUMBERS);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals("read book chapter 5", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedDateTime = LocalDateTime.of(2024, 6, 15, 0, 0);
        assertEquals(expectedDateTime, timeData[0]);
        assertEquals(false, timeData[1]);
    }
    
    @Test
    void shouldParseTaskWithSpecialCharacters() {
        ParseResult result = deadlineParser.parse("buy groceries & cook dinner /by 20-07-2024");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals("buy groceries & cook dinner", result.getArguments()[0]);
    }
    
    // Invalid deadline formats - regex pattern failures
    @Test
    void shouldRejectDeadlineWithMissingBy() {
        ParseResult result = deadlineParser.parse(INVALID_MISSING_BY);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithMissingTask() {
        ParseResult result = deadlineParser.parse(INVALID_MISSING_TASK);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithMissingSlash() {
        ParseResult result = deadlineParser.parse(INVALID_MISSING_SLASH);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEmptyString() {
        ParseResult result = deadlineParser.parse(EMPTY_STRING);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectJustBy() {
        ParseResult result = deadlineParser.parse(JUST_BY);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    // Invalid deadline formats - empty description or date
    @Test
    void shouldRejectDeadlineWithEmptyTask() {
        ParseResult result = deadlineParser.parse(INVALID_EMPTY_TASK);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithEmptyDate() {
        ParseResult result = deadlineParser.parse(INVALID_EMPTY_DATE);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithMissingDate() {
        ParseResult result = deadlineParser.parse(INVALID_MISSING_DATE);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithWhitespaceOnlyTask() {
        ParseResult result = deadlineParser.parse("   /by 25-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithWhitespaceOnlyDate() {
        ParseResult result = deadlineParser.parse("homework /by   ");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DEADLINE_FORMAT, result.getErrorMessage());
    }
    
    // Invalid deadline formats - date parsing failures
    @Test
    void shouldRejectDeadlineWithInvalidDateFormat() {
        ParseResult result = deadlineParser.parse(INVALID_DATE_FORMAT);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithInvalidDay() {
        ParseResult result = deadlineParser.parse("homework /by 32-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithInvalidMonth() {
        ParseResult result = deadlineParser.parse("homework /by 25-13-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithInvalidTime() {
        ParseResult result = deadlineParser.parse("homework /by 25-12-2023 25:00");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDeadlineWithWrongDateSeparator() {
        ParseResult result = deadlineParser.parse("homework /by 25/12/2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    // Edge cases
    @Test
    void shouldParseTaskWithMultipleByWords() {
        ParseResult result = deadlineParser.parse("task with by in name /by 25-12-2023");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals("task with by in name", result.getArguments()[0]);
    }
    
    @Test
    void shouldParseTaskWithSlashInName() {
        ParseResult result = deadlineParser.parse("read a/b testing book /by 25-12-2023");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals("read a/b testing book", result.getArguments()[0]);
    }
    
    @Test
    void shouldRejectMultipleByKeywords() {
        ParseResult result = deadlineParser.parse("task /by 25-12-2023 /by 26-12-2023");
        
        // This should still parse the first /by and treat the rest as part of the date string
        // which will then fail date parsing
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldHandleVeryLongTaskDescription() {
        String longTask = "this is a very long task description that goes on and on and describes many things in great detail about what needs to be done for this particular assignment or project";
        ParseResult result = deadlineParser.parse(longTask + " /by 25-12-2023");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.DEADLINE, result.getCommandType());
        assertEquals(longTask, result.getArguments()[0]);
    }
    
    // Test getCommandKeywords
    @Test
    void shouldReturnCorrectCommandKeywords() {
        String[] keywords = deadlineParser.getCommandKeywords();
        
        assertEquals(1, keywords.length);
        assertEquals("deadline", keywords[0]);
    }
}