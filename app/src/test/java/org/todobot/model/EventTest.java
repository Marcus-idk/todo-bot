package org.todobot.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTest {
    
    private Event event;
    private Event eventWithTime;
    private static final String TEST_DESCRIPTION = "Team meeting";
    private static final LocalDateTime TEST_FROM_DATE = LocalDateTime.of(2019, 12, 25, 14, 0);
    private static final LocalDateTime TEST_TO_DATE = LocalDateTime.of(2019, 12, 25, 16, 0);
    private static final LocalDateTime TEST_FROM_DATE_ONLY = LocalDateTime.of(2019, 12, 25, 0, 0);
    private static final LocalDateTime TEST_TO_DATE_ONLY = LocalDateTime.of(2019, 12, 26, 0, 0);
    private static final String EXPECTED_FROM_TIME = "25 Dec 2019, 1400";
    private static final String EXPECTED_TO_TIME = "25 Dec 2019, 1600";
    private static final String EXPECTED_FROM_DATE = "25 Dec 2019";
    private static final String EXPECTED_TO_DATE = "26 Dec 2019";
    
    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        event = new Event(TEST_DESCRIPTION, TEST_FROM_DATE_ONLY, false, TEST_TO_DATE_ONLY, false);
        eventWithTime = new Event(TEST_DESCRIPTION, TEST_FROM_DATE, true, TEST_TO_DATE, true);
    }
    
    @Test
    void shouldCreateEventWithDescriptionFromAndTo() {
        assertEquals(TEST_DESCRIPTION, event.getDescription());
        assertEquals(EXPECTED_FROM_DATE, event.getFrom());
        assertEquals(EXPECTED_TO_DATE, event.getTo());
        assertEquals(TEST_FROM_DATE_ONLY, event.getFromDateTime());
        assertEquals(TEST_TO_DATE_ONLY, event.getToDateTime());
        assertEquals(false, event.hasFromTime());
        assertEquals(false, event.hasToTime());
    }
    
    @Test
    void shouldCreateEventWithTime() {
        assertEquals(TEST_DESCRIPTION, eventWithTime.getDescription());
        assertEquals(EXPECTED_FROM_TIME, eventWithTime.getFrom());
        assertEquals(EXPECTED_TO_TIME, eventWithTime.getTo());
        assertEquals(TEST_FROM_DATE, eventWithTime.getFromDateTime());
        assertEquals(TEST_TO_DATE, eventWithTime.getToDateTime());
        assertEquals(true, eventWithTime.hasFromTime());
        assertEquals(true, eventWithTime.hasToTime());
    }
    
    @Test
    void shouldInitiallyBeNotDone() {
        assertFalse(event.isDone());
        assertEquals(" ", event.getStatusIcon());
    }
    
    @Test
    void shouldMarkEventAsDone() {
        event.markAsDone();
        assertTrue(event.isDone());
        assertEquals("X", event.getStatusIcon());
    }
    
    @Test
    void shouldMarkEventAsNotDone() {
        event.markAsDone();
        event.markAsNotDone();
        assertFalse(event.isDone());
        assertEquals(" ", event.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectTypeIcon() {
        assertEquals("E", event.getTypeIcon());
    }
    
    @Test
    void shouldReturnCorrectDetailsStringForDateOnly() {
        String expected = " (from: " + EXPECTED_FROM_DATE + " to: " + EXPECTED_TO_DATE + ")";
        assertEquals(expected, event.getDetailsString());
    }
    
    @Test
    void shouldReturnCorrectDetailsStringForDateTime() {
        String expected = " (from: " + EXPECTED_FROM_TIME + " to: " + EXPECTED_TO_TIME + ")";
        assertEquals(expected, eventWithTime.getDetailsString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenNotDone() {
        String expected = "[E][ ][M] " + TEST_DESCRIPTION + " (from: " + EXPECTED_FROM_DATE + " to: " + EXPECTED_TO_DATE + ")";
        assertEquals(expected, event.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenDone() {
        event.markAsDone();
        String expected = "[E][X][M] " + TEST_DESCRIPTION + " (from: " + EXPECTED_FROM_DATE + " to: " + EXPECTED_TO_DATE + ")";
        assertEquals(expected, event.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWithTime() {
        String expected = "[E][ ][M] " + TEST_DESCRIPTION + " (from: " + EXPECTED_FROM_TIME + " to: " + EXPECTED_TO_TIME + ")";
        assertEquals(expected, eventWithTime.toString());
    }
    
    @Test
    void shouldThrowExceptionForNullDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(null, TEST_FROM_DATE, true, TEST_TO_DATE, true)
        );
        assertEquals("Task description cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event("", TEST_FROM_DATE, true, TEST_TO_DATE, true)
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event("   ", TEST_FROM_DATE, true, TEST_TO_DATE, true)
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldHandleDifferentDateTimes() {
        LocalDateTime newYearFrom = LocalDateTime.of(2024, 1, 1, 10, 30);
        LocalDateTime newYearTo = LocalDateTime.of(2024, 1, 1, 12, 45);
        Event newYearEvent = new Event(TEST_DESCRIPTION, newYearFrom, true, newYearTo, true);
        assertEquals("01 Jan 2024, 1030", newYearEvent.getFrom());
        assertEquals("01 Jan 2024, 1245", newYearEvent.getTo());
        assertEquals("[E][ ][M] " + TEST_DESCRIPTION + " (from: 01 Jan 2024, 1030 to: 01 Jan 2024, 1245)", newYearEvent.toString());
    }
    
    @Test
    void shouldHandleMixedTimeFormats() {
        // From has time, To is date only
        LocalDateTime fromWithTime = LocalDateTime.of(2024, 6, 15, 9, 0);
        LocalDateTime toDateOnly = LocalDateTime.of(2024, 6, 16, 0, 0);
        Event mixedEvent = new Event(TEST_DESCRIPTION, fromWithTime, true, toDateOnly, false);
        assertEquals("15 Jun 2024, 0900", mixedEvent.getFrom());
        assertEquals("16 Jun 2024", mixedEvent.getTo());
        assertEquals("[E][ ][M] " + TEST_DESCRIPTION + " (from: 15 Jun 2024, 0900 to: 16 Jun 2024)", mixedEvent.toString());
    }
    
    @Test
    void shouldMaintainPolymorphismWithTaskReference() {
        LocalDateTime polymorphicFrom = LocalDateTime.of(2024, 3, 15, 10, 0);
        LocalDateTime polymorphicTo = LocalDateTime.of(2024, 3, 15, 11, 0);
        Task task = new Event("Polymorphic event", polymorphicFrom, true, polymorphicTo, true);
        assertEquals("E", task.getTypeIcon());
        assertEquals(" (from: 15 Mar 2024, 1000 to: 15 Mar 2024, 1100)", task.getDetailsString());
        assertEquals("[E][ ][M] Polymorphic event (from: 15 Mar 2024, 1000 to: 15 Mar 2024, 1100)", task.toString());
    }

    @Test
    void shouldThrowExceptionForNullFromDateTime() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, null, true, TEST_TO_DATE, true)
        );
        assertEquals("Event from time cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullToDateTime() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, TEST_FROM_DATE, true, null, true)
        );
        assertEquals("Event to time cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfFromAfterTo() {
        LocalDateTime invalidFrom = TEST_TO_DATE;
        LocalDateTime invalidTo = TEST_FROM_DATE;
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, invalidFrom, true, invalidTo, true)
        );
        assertEquals("Event start time must be before end time", exception.getMessage());
    }

    @Test
    void shouldHandleLeapYearEvents() {
        LocalDateTime leapDayStart = LocalDateTime.of(2024, 2, 29, 9, 0);
        LocalDateTime leapDayEnd = LocalDateTime.of(2024, 2, 29, 17, 0);
        Event leapDayEvent = new Event("Leap day conference", leapDayStart, true, leapDayEnd, true);
        assertEquals("29 Feb 2024, 0900", leapDayEvent.getFrom());
        assertEquals("29 Feb 2024, 1700", leapDayEvent.getTo());
    }
    
    @Test
    void shouldHandleEdgeCaseTimes() {
        // Midnight to midnight next day
        LocalDateTime startMidnight = LocalDateTime.of(2024, 6, 15, 0, 0);
        LocalDateTime endMidnight = LocalDateTime.of(2024, 6, 16, 0, 0);
        Event midnightEvent = new Event("All day event", startMidnight, true, endMidnight, true);
        assertEquals("15 Jun 2024, 0000", midnightEvent.getFrom());
        assertEquals("16 Jun 2024, 0000", midnightEvent.getTo());
        
        // Same exact time
        LocalDateTime sameTime = LocalDateTime.of(2024, 12, 25, 15, 30);
        Event instantEvent = new Event("Instant event", sameTime, true, sameTime, true);
        assertEquals("25 Dec 2024, 1530", instantEvent.getFrom());
        assertEquals("25 Dec 2024, 1530", instantEvent.getTo());
    }
    
    @Test
    void shouldHandleMultiDayEvents() {
        LocalDateTime conferenceStart = LocalDateTime.of(2024, 9, 16, 9, 0);
        LocalDateTime conferenceEnd = LocalDateTime.of(2024, 9, 20, 17, 0);
        Event multiDayEvent = new Event("Tech Conference", conferenceStart, true, conferenceEnd, true);
        assertEquals("16 Sep 2024, 0900", multiDayEvent.getFrom());
        assertEquals("20 Sep 2024, 1700", multiDayEvent.getTo());
        assertEquals("[E][ ][M] Tech Conference (from: 16 Sep 2024, 0900 to: 20 Sep 2024, 1700)", multiDayEvent.toString());
    }
    
}