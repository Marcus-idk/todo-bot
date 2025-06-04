package org.todobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.tasks.Event;
import org.todobot.tasks.Task;

public class EventTest {
    
    private Event event;
    private static final String TEST_DESCRIPTION = "Team meeting";
    private static final String TEST_FROM = "Mon 2pm";
    private static final String TEST_TO = "4pm";
    
    @BeforeEach
    void setUp() {
        event = new Event(TEST_DESCRIPTION, TEST_FROM, TEST_TO);
    }
    
    @Test
    void shouldCreateEventWithDescriptionFromAndTo() {
        assertEquals(TEST_DESCRIPTION, event.getDescription());
        assertEquals(TEST_FROM, event.getFrom());
        assertEquals(TEST_TO, event.getTo());
    }
    
    @Test
    void shouldInitiallyBeNotDone() {
        assertFalse(event.isDone());
    }
    
    @Test
    void shouldMarkEventAsDone() {
        event.markAsDone();
        assertTrue(event.isDone());
    }
    
    @Test
    void shouldMarkEventAsNotDone() {
        event.markAsDone();
        event.markAsNotDone();
        assertFalse(event.isDone());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenNotDone() {
        assertEquals(" ", event.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenDone() {
        event.markAsDone();
        assertEquals("X", event.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectTypeIcon() {
        assertEquals("E", event.getTypeIcon());
    }
    
    @Test
    void shouldReturnCorrectDetailsString() {
        String expected = " (from: " + TEST_FROM + " to: " + TEST_TO + ")";
        assertEquals(expected, event.getDetailsString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenNotDone() {
        String expected = "[E][ ] " + TEST_DESCRIPTION + " (from: " + TEST_FROM + " to: " + TEST_TO + ")";
        assertEquals(expected, event.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenDone() {
        event.markAsDone();
        String expected = "[E][X] " + TEST_DESCRIPTION + " (from: " + TEST_FROM + " to: " + TEST_TO + ")";
        assertEquals(expected, event.toString());
    }
    
    @Test
    void shouldReturnCorrectDescription() {
        assertEquals(TEST_DESCRIPTION, event.getDescription());
    }
    
    @Test
    void shouldReturnCorrectFrom() {
        assertEquals(TEST_FROM, event.getFrom());
    }
    
    @Test
    void shouldReturnCorrectTo() {
        assertEquals(TEST_TO, event.getTo());
    }
    
    @Test
    void shouldHandleMultipleMarkUnmarkOperations() {
        event.markAsDone();
        assertTrue(event.isDone());
        
        event.markAsNotDone();
        assertFalse(event.isDone());
        
        event.markAsDone();
        assertTrue(event.isDone());
        
        event.markAsNotDone();
        assertFalse(event.isDone());
    }
    
    @Test
    void shouldThrowExceptionForNullDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(null, TEST_FROM, TEST_TO)
        );
        assertEquals("Task description cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event("", TEST_FROM, TEST_TO)
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event("   ", TEST_FROM, TEST_TO)
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldHandleDescriptionWithSpaces() {
        String spaceyDescription = "Important   team   meeting";
        Event spaceyEvent = new Event(spaceyDescription, TEST_FROM, TEST_TO);
        assertEquals(spaceyDescription, spaceyEvent.getDescription());
        assertEquals("[E][ ] " + spaceyDescription + " (from: " + TEST_FROM + " to: " + TEST_TO + ")", spaceyEvent.toString());
    }
    
    @Test
    void shouldHandleSpecialCharactersInDescription() {
        String specialDescription = "Q&A session #1 @ 100% attendance!";
        Event specialEvent = new Event(specialDescription, TEST_FROM, TEST_TO);
        assertEquals(specialDescription, specialEvent.getDescription());
        assertEquals("[E][ ] " + specialDescription + " (from: " + TEST_FROM + " to: " + TEST_TO + ")", specialEvent.toString());
    }
    
    @Test
    void shouldHandleSpecialCharactersInFromAndTo() {
        String specialFrom = "Mon @ 2:30pm";
        String specialTo = "4:45pm :-) ";
        Event specialEvent = new Event(TEST_DESCRIPTION, specialFrom, specialTo);
        assertEquals(specialFrom, specialEvent.getFrom());
        assertEquals(specialTo, specialEvent.getTo());
        assertEquals("[E][ ] " + TEST_DESCRIPTION + " (from: " + specialFrom + " to: " + specialTo + ")", specialEvent.toString());
    }
    
    @Test
    void shouldHandleVeryLongDescription() {
        String longDescription = "Comprehensive project planning meeting to discuss timeline, deliverables, resource allocation and risk management strategies";
        Event longEvent = new Event(longDescription, TEST_FROM, TEST_TO);
        assertEquals(longDescription, longEvent.getDescription());
        assertEquals("[E][ ] " + longDescription + " (from: " + TEST_FROM + " to: " + TEST_TO + ")", longEvent.toString());
    }
    
    @Test
    void shouldHandleVeryLongFromAndTo() {
        String longFrom = "Monday at 2pm in the main conference room";
        String longTo = "Wednesday at 5pm in the same location";
        Event longEvent = new Event(TEST_DESCRIPTION, longFrom, longTo);
        assertEquals(longFrom, longEvent.getFrom());
        assertEquals(longTo, longEvent.getTo());
        assertEquals("[E][ ] " + TEST_DESCRIPTION + " (from: " + longFrom + " to: " + longTo + ")", longEvent.toString());
    }
    
    @Test
    void shouldMaintainPolymorphismWithTaskReference() {
        Task task = new Event("Polymorphic event", "start", "end");
        assertEquals("E", task.getTypeIcon());
        assertEquals(" (from: start to: end)", task.getDetailsString());
        assertEquals("[E][ ] Polymorphic event (from: start to: end)", task.toString());
    }

    @Test
    void shouldThrowExceptionForNullFrom() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, null, TEST_TO)
        );
        assertEquals("Event from time cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullTo() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, TEST_FROM, null)
        );
        assertEquals("Event to time cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyFrom() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, "", TEST_TO)
        );
        assertEquals("Event from time cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyTo() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, TEST_FROM, "")
        );
        assertEquals("Event to time cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyFrom() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, "   ", TEST_TO)
        );
        assertEquals("Event from time cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyTo() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Event(TEST_DESCRIPTION, TEST_FROM, "   ")
        );
        assertEquals("Event to time cannot be empty", exception.getMessage());
    }

    @Test
    void shouldHandleUnicodeCharactersInTimes() {
        String unicodeFrom = "الإثنين في الصباح";
        String unicodeTo = "الثلاثاء في المساء";
        Event unicodeEvent = new Event(TEST_DESCRIPTION, unicodeFrom, unicodeTo);
        assertEquals(unicodeFrom, unicodeEvent.getFrom());
        assertEquals(unicodeTo, unicodeEvent.getTo());
        assertEquals("[E][ ] " + TEST_DESCRIPTION + " (from: " + unicodeFrom + " to: " + unicodeTo + ")", unicodeEvent.toString());
    }
    
    @Test
    void shouldHandleCommonDateTimeFormats() {
        String[][] timeFormats = {
            {"2023-12-25 09:00", "2023-12-25 17:00"},
            {"Dec 25 9am", "Dec 25 5pm"},
            {"Monday morning", "Monday evening"},
            {"next week", "following week"},
            {"Jan 1st 2024", "Jan 2nd 2024"},
            {"2pm", "4pm"},
            {"14:00", "16:00"}
        };
        
        for (String[] timeFormat : timeFormats) {
            Event dateEvent = new Event("Test event", timeFormat[0], timeFormat[1]);
            assertEquals(timeFormat[0], dateEvent.getFrom());
            assertEquals(timeFormat[1], dateEvent.getTo());
            assertEquals(" (from: " + timeFormat[0] + " to: " + timeFormat[1] + ")", dateEvent.getDetailsString());
        }
    }
    
    @Test
    void shouldHandleSameFromAndToTimes() {
        String sameTime = "3pm";
        Event sameTimeEvent = new Event(TEST_DESCRIPTION, sameTime, sameTime);
        assertEquals(sameTime, sameTimeEvent.getFrom());
        assertEquals(sameTime, sameTimeEvent.getTo());
        assertEquals(" (from: " + sameTime + " to: " + sameTime + ")", sameTimeEvent.getDetailsString());
    }
    
    @Test
    void shouldHandleMultiDayEvents() {
        Event multiDayEvent = new Event("Conference", "Monday 9am", "Friday 5pm");
        assertEquals("Monday 9am", multiDayEvent.getFrom());
        assertEquals("Friday 5pm", multiDayEvent.getTo());
        assertEquals("[E][ ] Conference (from: Monday 9am to: Friday 5pm)", multiDayEvent.toString());
    }
}