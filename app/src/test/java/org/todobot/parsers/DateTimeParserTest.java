package org.todobot.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;
import org.todobot.parsers.util.DateTimeParser;
import org.todobot.parsers.util.DateTimeParser.DateTimeResult;

public class DateTimeParserTest {
    
    private static final String VALID_DATE = "25-12-2023";
    private static final String VALID_DATE_LEAP_YEAR = "29-02-2024";
    private static final String VALID_DATE_BOUNDARY = "31-12-2023";
    private static final String VALID_DATE_FIRST_OF_YEAR = "01-01-2024";
    private static final String VALID_DATETIME = "25-12-2023 14:30";
    private static final String VALID_DATETIME_MIDNIGHT = "01-01-2024 00:00";
    private static final String VALID_DATETIME_END_OF_DAY = "31-12-2023 23:59";
    private static final String VALID_DATETIME_NOON = "15-06-2024 12:00";
    private static final String VALID_DATE_WITH_SPACES = "  25-12-2023  ";
    private static final String VALID_DATETIME_WITH_SPACES = "  25-12-2023 14:30  ";
    
    private static final String INVALID_DAY = "32-12-2023";
    private static final String INVALID_MONTH = "25-13-2023";
    private static final String INVALID_YEAR_FORMAT = "25-12-23";
    private static final String INVALID_SEPARATOR = "25/12/2023";
    private static final String INVALID_HOUR = "25-12-2023 25:00";
    private static final String INVALID_MINUTE = "25-12-2023 14:70";
    private static final String INVALID_INCOMPLETE_DATETIME = "25-12-2023 14:";
    private static final String NON_NUMERIC = "invalid-date";
    private static final String EMPTY_STRING = "";
    private static final String WHITESPACE_ONLY = "   ";
    
    // parseDateTime() - Valid date formats
    @Test
    void shouldParseValidDateFormat() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATE);
        
        LocalDateTime expected = LocalDateTime.of(2023, 12, 25, 0, 0);
        assertEquals(expected, result.getDateTime());
        assertFalse(result.hasTime());
    }
    
    @Test
    void shouldParseLeapYearDate() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATE_LEAP_YEAR);
        
        LocalDateTime expected = LocalDateTime.of(2024, 2, 29, 0, 0);
        assertEquals(expected, result.getDateTime());
        assertFalse(result.hasTime());
    }
    
    // parseDateTime() - Valid datetime formats
    @Test
    void shouldParseValidDateTimeFormat() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATETIME);
        
        LocalDateTime expected = LocalDateTime.of(2023, 12, 25, 14, 30);
        assertEquals(expected, result.getDateTime());
        assertTrue(result.hasTime());
    }
    
    @Test
    void shouldParseMidnightDateTime() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATETIME_MIDNIGHT);
        
        LocalDateTime expected = LocalDateTime.of(2024, 1, 1, 0, 0);
        assertEquals(expected, result.getDateTime());
        assertTrue(result.hasTime());
    }
    
    // parseDateTime() - Invalid formats and exceptions
    @Test
    void shouldThrowExceptionForEmptyOrNullInputs() {
        DateTimeParseException exception1 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(null);
        });
        assertEquals("Date/time input cannot be empty", exception1.getMessage());
        
        DateTimeParseException exception2 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(EMPTY_STRING);
        });
        assertEquals("Date/time input cannot be empty", exception2.getMessage());
        
        DateTimeParseException exception3 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(WHITESPACE_ONLY);
        });
        assertEquals("Date/time input cannot be empty", exception3.getMessage());
    }
    
    @Test
    void shouldThrowExceptionForInvalidDay() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_DAY);
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidMonth() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_MONTH);
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidTimeComponents() {
        DateTimeParseException exception1 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_HOUR);
        });
        assertTrue(exception1.getMessage().contains("Invalid date/time format"));
        
        DateTimeParseException exception2 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_MINUTE);
        });
        assertTrue(exception2.getMessage().contains("Invalid date/time format"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidFormats() {
        DateTimeParseException exception1 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_YEAR_FORMAT);
        });
        assertTrue(exception1.getMessage().contains("Invalid format"));
        
        DateTimeParseException exception2 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_SEPARATOR);
        });
        assertTrue(exception2.getMessage().contains("Invalid format"));
        
        DateTimeParseException exception3 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(NON_NUMERIC);
        });
        assertTrue(exception3.getMessage().contains("Invalid format"));
        
        DateTimeParseException exception4 = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_INCOMPLETE_DATETIME);
        });
        assertTrue(exception4.getMessage().contains("Invalid format"));
    }
    
    // parseDateTime() - Whitespace handling
    @Test
    void shouldHandleLeadingAndTrailingWhitespace() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATE_WITH_SPACES);
        
        LocalDateTime expected = LocalDateTime.of(2023, 12, 25, 0, 0);
        assertEquals(expected, result.getDateTime());
        assertFalse(result.hasTime());
    }
    
    @Test
    void shouldHandleWhitespaceInDateTime() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATETIME_WITH_SPACES);
        
        LocalDateTime expected = LocalDateTime.of(2023, 12, 25, 14, 30);
        assertEquals(expected, result.getDateTime());
        assertTrue(result.hasTime());
    }
    
    // formatDateTime() method tests
    @Test
    void shouldFormatDateOnly() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 14, 30);
        String result = DateTimeParser.formatDateTime(dateTime, false);
        
        assertEquals("25 Dec 2023", result);
    }
    
    @Test
    void shouldFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 14, 30);
        String result = DateTimeParser.formatDateTime(dateTime, true);
        
        assertEquals("25 Dec 2023, 1430", result);
    }
    
}