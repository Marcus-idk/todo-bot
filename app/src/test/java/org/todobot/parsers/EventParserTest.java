package org.todobot.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;

public class EventParserTest {
    
    private EventParser eventParser;
    
    private static final String VALID_EVENT_DATES = "meeting /from 25-12-2023 /to 26-12-2023";
    private static final String VALID_EVENT_DATETIMES = "conference /from 01-01-2024 09:00 /to 01-01-2024 17:00";
    private static final String VALID_EVENT_MIXED_TIMES = "workshop /from 15-06-2024 /to 15-06-2024 18:00";
    private static final String VALID_EVENT_WITH_SPACES = "   project review   /from   25-12-2023   /to   26-12-2023   ";
    private static final String VALID_COMPLEX_EVENT = "annual company retreat planning session /from 01-07-2024 08:30 /to 03-07-2024 17:30";
    
    private static final String INVALID_MISSING_FROM = "meeting 25-12-2023 /to 26-12-2023";
    private static final String INVALID_MISSING_TO = "meeting /from 25-12-2023 26-12-2023";
    private static final String INVALID_MISSING_TASK = "/from 25-12-2023 /to 26-12-2023";
    private static final String INVALID_MISSING_FROM_DATE = "meeting /from /to 26-12-2023";
    private static final String INVALID_MISSING_TO_DATE = "meeting /from 25-12-2023 /to";
    private static final String INVALID_WRONG_ORDER = "meeting /to 26-12-2023 /from 25-12-2023";
    private static final String INVALID_FROM_DATE_FORMAT = "meeting /from invalid-date /to 26-12-2023";
    private static final String INVALID_TO_DATE_FORMAT = "meeting /from 25-12-2023 /to invalid-date";
    private static final String EMPTY_STRING = "";
    
    @BeforeEach
    void setUp() {
        eventParser = new EventParser();
    }
    
    // Valid event formats
    @Test
    void shouldParseValidEventWithDates() {
        ParseResult result = eventParser.parse(VALID_EVENT_DATES);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("meeting", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2023, 12, 26, 0, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]); // from dateTime
        assertFalse((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]); // to dateTime
        assertFalse((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldParseValidEventWithDateTimes() {
        ParseResult result = eventParser.parse(VALID_EVENT_DATETIMES);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("conference", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2024, 1, 1, 9, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2024, 1, 1, 17, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertTrue((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertTrue((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldParseEventWithMixedDateAndDateTime() {
        ParseResult result = eventParser.parse(VALID_EVENT_MIXED_TIMES);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("workshop", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2024, 6, 15, 0, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2024, 6, 15, 18, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertFalse((Boolean) timeData[1]); // from date only
        assertEquals(expectedToDateTime, timeData[2]);
        assertTrue((Boolean) timeData[3]); // to has time
    }
    
    @Test
    void shouldParseEventWithLeadingAndTrailingSpaces() {
        ParseResult result = eventParser.parse(VALID_EVENT_WITH_SPACES);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("project review", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2023, 12, 26, 0, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertFalse((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertFalse((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldParseComplexEventDescription() {
        ParseResult result = eventParser.parse(VALID_COMPLEX_EVENT);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("annual company retreat planning session", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2024, 7, 1, 8, 30);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2024, 7, 3, 17, 30);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertTrue((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertTrue((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldParseSameDayEvent() {
        ParseResult result = eventParser.parse("daily standup /from 15-06-2024 09:00 /to 15-06-2024 09:30");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("daily standup", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2024, 6, 15, 9, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2024, 6, 15, 9, 30);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertTrue((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertTrue((Boolean) timeData[3]); // to hasTime
    }
    
    // Invalid event formats - regex pattern failures
    @Test
    void shouldRejectEventWithMissingFrom() {
        ParseResult result = eventParser.parse(INVALID_MISSING_FROM);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithMissingTo() {
        ParseResult result = eventParser.parse(INVALID_MISSING_TO);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithMissingTask() {
        ParseResult result = eventParser.parse(INVALID_MISSING_TASK);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithWrongOrder() {
        ParseResult result = eventParser.parse(INVALID_WRONG_ORDER);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEmptyString() {
        ParseResult result = eventParser.parse(EMPTY_STRING);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithoutSlashes() {
        ParseResult result = eventParser.parse("meeting from 25-12-2023 to 26-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    // Invalid event formats - empty fields
    @Test
    void shouldRejectEventWithEmptyTask() {
        ParseResult result = eventParser.parse(" /from 25-12-2023 /to 26-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithEmptyFromDate() {
        ParseResult result = eventParser.parse(INVALID_MISSING_FROM_DATE);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithEmptyToDate() {
        ParseResult result = eventParser.parse(INVALID_MISSING_TO_DATE);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithWhitespaceOnlyTask() {
        ParseResult result = eventParser.parse("   /from 25-12-2023 /to 26-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithWhitespaceOnlyFromDate() {
        ParseResult result = eventParser.parse("meeting /from   /to 26-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithWhitespaceOnlyToDate() {
        ParseResult result = eventParser.parse("meeting /from 25-12-2023 /to   ");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_EVENT_FORMAT, result.getErrorMessage());
    }
    
    // Invalid event formats - date parsing failures
    @Test
    void shouldRejectEventWithInvalidFromDate() {
        ParseResult result = eventParser.parse(INVALID_FROM_DATE_FORMAT);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithInvalidToDate() {
        ParseResult result = eventParser.parse(INVALID_TO_DATE_FORMAT);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithInvalidFromDay() {
        ParseResult result = eventParser.parse("meeting /from 32-12-2023 /to 26-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithInvalidToMonth() {
        ParseResult result = eventParser.parse("meeting /from 25-12-2023 /to 26-13-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithInvalidFromTime() {
        ParseResult result = eventParser.parse("meeting /from 25-12-2023 25:00 /to 26-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithInvalidToTime() {
        ParseResult result = eventParser.parse("meeting /from 25-12-2023 /to 26-12-2023 25:00");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithWrongFromDateSeparator() {
        ParseResult result = eventParser.parse("meeting /from 25/12/2023 /to 26-12-2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEventWithWrongToDateSeparator() {
        ParseResult result = eventParser.parse("meeting /from 25-12-2023 /to 26/12/2023");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    // Edge cases
    @Test
    void shouldParseEventWithFromAndToInDescription() {
        ParseResult result = eventParser.parse("journey from home to office /from 25-12-2023 /to 26-12-2023");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("journey from home to office", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2023, 12, 26, 0, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertFalse((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertFalse((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldParseEventWithSlashInDescription() {
        ParseResult result = eventParser.parse("read a/b testing manual /from 25-12-2023 /to 26-12-2023");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("read a/b testing manual", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2023, 12, 26, 0, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertFalse((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertFalse((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldParseEventWithNumbersInDescription() {
        ParseResult result = eventParser.parse("meeting room 101 booking /from 25-12-2023 09:00 /to 25-12-2023 17:00");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("meeting room 101 booking", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2023, 12, 25, 9, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2023, 12, 25, 17, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertTrue((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertTrue((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldParseEventWithSpecialCharacters() {
        ParseResult result = eventParser.parse("Q&A session & networking /from 25-12-2023 /to 26-12-2023");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("Q&A session & networking", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2023, 12, 26, 0, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertFalse((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertFalse((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldRejectMultipleFromToKeywords() {
        ParseResult result = eventParser.parse("meeting /from 25-12-2023 /to 26-12-2023 /from 27-12-2023 /to 28-12-2023");
        
        // This should parse the first /from and /to, then fail when trying to parse the extra text as date
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_DATE_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldHandleVeryLongTaskDescription() {
        String longTask = "this is an extremely long event description that contains many words and describes in great detail what the event is about and what participants should expect during the event";
        ParseResult result = eventParser.parse(longTask + " /from 25-12-2023 /to 26-12-2023");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals(longTask, result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2023, 12, 26, 0, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertFalse((Boolean) timeData[1]); // from hasTime
        assertEquals(expectedToDateTime, timeData[2]);
        assertFalse((Boolean) timeData[3]); // to hasTime
    }
    
    @Test
    void shouldParseOverlappingTimeRanges() {
        // This tests that we don't validate business logic (like from > to), just parsing
        ParseResult result = eventParser.parse("meeting /from 26-12-2023 /to 25-12-2023");
        
        assertTrue(result.isValid());
        assertEquals(CommandType.EVENT, result.getCommandType());
        assertEquals("meeting", result.getArguments()[0]);
        
        Object[] timeData = result.getTimeData();
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2023, 12, 26, 0, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        
        assertEquals(expectedFromDateTime, timeData[0]);
        assertEquals(expectedToDateTime, timeData[2]);
    }
    
    // Test getCommandKeywords
    @Test
    void shouldReturnCorrectCommandKeywords() {
        String[] keywords = eventParser.getCommandKeywords();
        
        assertEquals(1, keywords.length);
        assertEquals("event", keywords[0]);
    }
}