package org.todobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.tasks.Deadline;

public class DeadlineTest {
    
    private Deadline deadline;
    private static final String TEST_DESCRIPTION = "Submit report";
    private static final String TEST_BY = "Friday 5pm";
    
    @BeforeEach
    void setUp() {
        deadline = new Deadline(TEST_DESCRIPTION, TEST_BY);
    }
    
    @Test
    void shouldCreateDeadlineWithDescriptionAndBy() {
        assertEquals(TEST_DESCRIPTION, deadline.getDescription());
        assertEquals(TEST_BY, deadline.getBy());
    }
    
    @Test
    void shouldInitiallyBeNotDone() {
        assertFalse(deadline.isDone());
    }
    
    @Test
    void shouldMarkDeadlineAsDone() {
        deadline.markAsDone();
        assertTrue(deadline.isDone());
    }
    
    @Test
    void shouldMarkDeadlineAsNotDone() {
        deadline.markAsDone();
        deadline.markAsNotDone();
        assertFalse(deadline.isDone());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenNotDone() {
        assertEquals(" ", deadline.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenDone() {
        deadline.markAsDone();
        assertEquals("X", deadline.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectTypeIcon() {
        assertEquals("D", deadline.getTypeIcon());
    }
    
    @Test
    void shouldReturnCorrectDetailsString() {
        String expected = " (by: " + TEST_BY + ")";
        assertEquals(expected, deadline.getDetailsString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenNotDone() {
        String expected = "[D][ ] " + TEST_DESCRIPTION + " (by: " + TEST_BY + ")";
        assertEquals(expected, deadline.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenDone() {
        deadline.markAsDone();
        String expected = "[D][X] " + TEST_DESCRIPTION + " (by: " + TEST_BY + ")";
        assertEquals(expected, deadline.toString());
    }
    
    @Test
    void shouldReturnCorrectDescription() {
        assertEquals(TEST_DESCRIPTION, deadline.getDescription());
    }
    
    @Test
    void shouldReturnCorrectBy() {
        assertEquals(TEST_BY, deadline.getBy());
    }
    
    @Test
    void shouldHandleMultipleMarkUnmarkOperations() {
        deadline.markAsDone();
        assertTrue(deadline.isDone());
        
        deadline.markAsNotDone();
        assertFalse(deadline.isDone());
        
        deadline.markAsDone();
        assertTrue(deadline.isDone());
        
        deadline.markAsNotDone();
        assertFalse(deadline.isDone());
    }
    
    @Test
    void shouldThrowExceptionForNullDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline(null, TEST_BY)
        );
        assertEquals("Task description cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline("", TEST_BY)
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline("   ", TEST_BY)
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldHandleDescriptionWithSpaces() {
        String spaceyDescription = "Submit   final   report";
        Deadline spaceyDeadline = new Deadline(spaceyDescription, TEST_BY);
        assertEquals(spaceyDescription, spaceyDeadline.getDescription());
        assertEquals("[D][ ] " + spaceyDescription + " (by: " + TEST_BY + ")", spaceyDeadline.toString());
    }
    
    @Test
    void shouldHandleSpecialCharactersInDescription() {
        String specialDescription = "Submit report #2 @ 100% completion!";
        Deadline specialDeadline = new Deadline(specialDescription, TEST_BY);
        assertEquals(specialDescription, specialDeadline.getDescription());
        assertEquals("[D][ ] " + specialDescription + " (by: " + TEST_BY + ")", specialDeadline.toString());
    }
    
    @Test
    void shouldHandleSpecialCharactersInBy() {
        String specialBy = "no idea :-p";
        Deadline specialDeadline = new Deadline(TEST_DESCRIPTION, specialBy);
        assertEquals(specialBy, specialDeadline.getBy());
        assertEquals("[D][ ] " + TEST_DESCRIPTION + " (by: " + specialBy + ")", specialDeadline.toString());
    }
    
    @Test
    void shouldHandleVeryLongDescription() {
        String longDescription = "Submit a very comprehensive and detailed report covering all aspects of the project including methodology, results, conclusions and recommendations for future work";
        Deadline longDeadline = new Deadline(longDescription, TEST_BY);
        assertEquals(longDescription, longDeadline.getDescription());
        assertEquals("[D][ ] " + longDescription + " (by: " + TEST_BY + ")", longDeadline.toString());
    }
    
    @Test
    void shouldHandleVeryLongBy() {
        String longBy = "next Friday at 5pm sharp after the weekly team meeting concludes";
        Deadline longDeadline = new Deadline(TEST_DESCRIPTION, longBy);
        assertEquals(longBy, longDeadline.getBy());
        assertEquals("[D][ ] " + TEST_DESCRIPTION + " (by: " + longBy + ")", longDeadline.toString());
    }

    @Test
    void shouldThrowExceptionForNullBy() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline(TEST_DESCRIPTION, null)
        );
        assertEquals("Deadline by date cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyBy() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline(TEST_DESCRIPTION, "")
        );
        assertEquals("Deadline by date cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyBy() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Deadline(TEST_DESCRIPTION, "   ")
        );
        assertEquals("Deadline by date cannot be empty", exception.getMessage());
    }

    @Test
    void shouldHandleUnicodeCharactersInBy() {
        String unicodeBy = "الجمعة في المساء";
        Deadline unicodeDeadline = new Deadline(TEST_DESCRIPTION, unicodeBy);
        assertEquals(unicodeBy, unicodeDeadline.getBy());
        assertEquals("[D][ ] " + TEST_DESCRIPTION + " (by: " + unicodeBy + ")", unicodeDeadline.toString());
    }
    
    @Test
    void shouldHandleCommonDateFormats() {
        String[] dateFormats = {
            "2023-12-25",
            "Dec 25, 2023",
            "25/12/2023",
            "Monday",
            "next week",
            "tomorrow at 3pm",
            "2023-12-25 15:30"
        };
        
        for (String dateFormat : dateFormats) {
            Deadline dateDeadline = new Deadline("Test task", dateFormat);
            assertEquals(dateFormat, dateDeadline.getBy());
            assertEquals(" (by: " + dateFormat + ")", dateDeadline.getDetailsString());
        }
    }
}