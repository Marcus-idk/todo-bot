package org.todobot.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeadlineTest {
    
    private Deadline deadline;
    private Deadline deadlineWithTime;
    private static final String TEST_DESCRIPTION = "Submit report";
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2019, 12, 25, 14, 30);
    private static final LocalDateTime TEST_DATE_ONLY = LocalDateTime.of(2019, 12, 25, 0, 0);
    private static final String EXPECTED_DATE_ONLY = "25 Dec 2019";
    private static final String EXPECTED_DATE_TIME = "25 Dec 2019, 1430";
    
    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        deadline = new Deadline(TEST_DESCRIPTION, TEST_DATE_ONLY, false);
        deadlineWithTime = new Deadline(TEST_DESCRIPTION, TEST_DATE_TIME, true);
    }
    
    @Test
    void shouldCreateDeadlineWithDescriptionAndDateTime() {
        assertEquals(TEST_DESCRIPTION, deadline.getDescription());
        assertEquals(EXPECTED_DATE_ONLY, deadline.getBy());
        assertEquals(TEST_DATE_ONLY, deadline.getByDateTime());
        assertEquals(false, deadline.hasTimeInfo());
    }
    
    @Test
    void shouldCreateDeadlineWithTime() {
        assertEquals(TEST_DESCRIPTION, deadlineWithTime.getDescription());
        assertEquals(EXPECTED_DATE_TIME, deadlineWithTime.getBy());
        assertEquals(TEST_DATE_TIME, deadlineWithTime.getByDateTime());
        assertEquals(true, deadlineWithTime.hasTimeInfo());
    }
    
    @Test
    void shouldInitiallyBeNotDone() {
        assertFalse(deadline.isDone());
        assertEquals(" ", deadline.getStatusIcon());
    }
    
    @Test
    void shouldMarkDeadlineAsDone() {
        deadline.markAsDone();
        assertTrue(deadline.isDone());
        assertEquals("X", deadline.getStatusIcon());
    }
    
    @Test
    void shouldMarkDeadlineAsNotDone() {
        deadline.markAsDone();
        deadline.markAsNotDone();
        assertFalse(deadline.isDone());
        assertEquals(" ", deadline.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectTypeIcon() {
        assertEquals("D", deadline.getTypeIcon());
    }
    
    @Test
    void shouldReturnCorrectDetailsStringForDateOnly() {
        String expected = " (by: " + EXPECTED_DATE_ONLY + ")";
        assertEquals(expected, deadline.getDetailsString());
    }
    
    @Test
    void shouldReturnCorrectDetailsStringForDateTime() {
        String expected = " (by: " + EXPECTED_DATE_TIME + ")";
        assertEquals(expected, deadlineWithTime.getDetailsString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenNotDone() {
        String expected = "[D][ ][M] " + TEST_DESCRIPTION + " (by: " + EXPECTED_DATE_ONLY + ")";
        assertEquals(expected, deadline.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenDone() {
        deadline.markAsDone();
        String expected = "[D][X][M] " + TEST_DESCRIPTION + " (by: " + EXPECTED_DATE_ONLY + ")";
        assertEquals(expected, deadline.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWithTime() {
        String expected = "[D][ ][M] " + TEST_DESCRIPTION + " (by: " + EXPECTED_DATE_TIME + ")";
        assertEquals(expected, deadlineWithTime.toString());
    }
    
    @Test
    void shouldThrowExceptionForNullDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline(null, TEST_DATE_ONLY, false)
        );
        assertEquals("Task description cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline("", TEST_DATE_ONLY, false)
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline("   ", TEST_DATE_ONLY, false)
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldHandleMidnightTime() {
        LocalDateTime midnight = LocalDateTime.of(2019, 12, 31, 0, 0);
        Deadline midnightDeadline = new Deadline(TEST_DESCRIPTION, midnight, true);
        assertEquals("31 Dec 2019, 0000", midnightDeadline.getBy());
        assertEquals("[D][ ][M] " + TEST_DESCRIPTION + " (by: 31 Dec 2019, 0000)", midnightDeadline.toString());
    }

    @Test
    void shouldThrowExceptionForNullDateTime() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline(TEST_DESCRIPTION, null, false)
        );
        assertEquals("Deadline by date cannot be null", exception.getMessage());
    }

    @Test
    void shouldHandleDateOnlyVsDateTime() {
        // Date only
        LocalDateTime dateOnly = LocalDateTime.of(2024, 6, 15, 0, 0);
        Deadline dateOnlyDeadline = new Deadline("Task 1", dateOnly, false);
        assertEquals("15 Jun 2024", dateOnlyDeadline.getBy());
        assertEquals(false, dateOnlyDeadline.hasTimeInfo());
        
        // Date with time  
        LocalDateTime dateTime = LocalDateTime.of(2024, 6, 15, 15, 45);
        Deadline dateTimeDeadline = new Deadline("Task 2", dateTime, true);
        assertEquals("15 Jun 2024, 1545", dateTimeDeadline.getBy());
        assertEquals(true, dateTimeDeadline.hasTimeInfo());
    }
    
    @Test
    void shouldHandleLeapYear() {
        LocalDateTime leapDay = LocalDateTime.of(2024, 2, 29, 12, 0);
        Deadline leapDeadline = new Deadline("Leap year task", leapDay, true);
        assertEquals("29 Feb 2024, 1200", leapDeadline.getBy());
    }
    
    @Test
    void shouldHandleEdgeCaseTimes() {
        // 12:01 AM
        LocalDateTime earlyMorning = LocalDateTime.of(2024, 1, 1, 0, 1);
        Deadline earlyDeadline = new Deadline("Early task", earlyMorning, true);
        assertEquals("01 Jan 2024, 0001", earlyDeadline.getBy());
        
        // 11:59 PM
        LocalDateTime lateNight = LocalDateTime.of(2024, 12, 31, 23, 59);
        Deadline lateDeadline = new Deadline("Late task", lateNight, true);
        assertEquals("31 Dec 2024, 2359", lateDeadline.getBy());
    }
    
}