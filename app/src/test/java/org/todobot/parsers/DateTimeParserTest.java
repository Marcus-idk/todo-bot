package org.todobot.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;
import org.todobot.parsers.DateTimeParser.DateTimeResult;

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
    
    @Test
    void shouldParseBoundaryDate() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATE_BOUNDARY);
        
        LocalDateTime expected = LocalDateTime.of(2023, 12, 31, 0, 0);
        assertEquals(expected, result.getDateTime());
        assertFalse(result.hasTime());
    }
    
    @Test
    void shouldParseFirstOfYear() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATE_FIRST_OF_YEAR);
        
        LocalDateTime expected = LocalDateTime.of(2024, 1, 1, 0, 0);
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
    
    @Test
    void shouldParseEndOfDayDateTime() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATETIME_END_OF_DAY);
        
        LocalDateTime expected = LocalDateTime.of(2023, 12, 31, 23, 59);
        assertEquals(expected, result.getDateTime());
        assertTrue(result.hasTime());
    }
    
    @Test
    void shouldParseNoonDateTime() {
        DateTimeResult result = DateTimeParser.parseDateTime(VALID_DATETIME_NOON);
        
        LocalDateTime expected = LocalDateTime.of(2024, 6, 15, 12, 0);
        assertEquals(expected, result.getDateTime());
        assertTrue(result.hasTime());
    }
    
    // parseDateTime() - Invalid formats and exceptions
    @Test
    void shouldThrowExceptionForNullInput() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(null);
        });
        assertEquals("Date/time input cannot be empty", exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionForEmptyInput() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(EMPTY_STRING);
        });
        assertEquals("Date/time input cannot be empty", exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionForWhitespaceOnlyInput() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(WHITESPACE_ONLY);
        });
        assertEquals("Date/time input cannot be empty", exception.getMessage());
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
    void shouldThrowExceptionForInvalidYearFormat() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_YEAR_FORMAT);
        });
        assertTrue(exception.getMessage().contains("Invalid format"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidSeparator() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_SEPARATOR);
        });
        assertTrue(exception.getMessage().contains("Invalid format"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidHour() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_HOUR);
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidMinute() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_MINUTE);
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format"));
    }
    
    @Test
    void shouldThrowExceptionForNonNumericInput() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(NON_NUMERIC);
        });
        assertTrue(exception.getMessage().contains("Invalid format"));
    }
    
    @Test
    void shouldThrowExceptionForIncompleteDateTime() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(INVALID_INCOMPLETE_DATETIME);
        });
        assertTrue(exception.getMessage().contains("Invalid format"));
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
    
    @Test
    void shouldFormatMidnight() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        String result = DateTimeParser.formatDateTime(dateTime, true);
        
        assertEquals("01 Jan 2024, 0000", result);
    }
    
    @Test  
    void shouldFormatSingleDigitDay() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 5, 9, 5);
        String result = DateTimeParser.formatDateTime(dateTime, true);
        
        assertEquals("05 Jan 2024, 0905", result);
    }
    
    // DateTimeResult inner class tests
    @Test
    void shouldCreateDateTimeResultWithDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 14, 30);
        DateTimeResult result = new DateTimeResult(dateTime, true);
        
        assertEquals(dateTime, result.getDateTime());
        assertTrue(result.hasTime());
    }
    
    @Test
    void shouldCreateDateTimeResultWithDateOnly() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 0, 0);
        DateTimeResult result = new DateTimeResult(dateTime, false);
        
        assertEquals(dateTime, result.getDateTime());
        assertFalse(result.hasTime());
    }
}