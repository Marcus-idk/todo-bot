package org.todobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class PriorityTest {

    @Test
    void shouldReturnCorrectIcons() {
        assertEquals("L", Priority.LOW.getIcon());
        assertEquals("M", Priority.MEDIUM.getIcon());
        assertEquals("H", Priority.HIGH.getIcon());
    }

    @Test
    void shouldSupportValueOf() {
        assertEquals(Priority.LOW, Priority.valueOf("LOW"));
        assertEquals(Priority.MEDIUM, Priority.valueOf("MEDIUM"));
        assertEquals(Priority.HIGH, Priority.valueOf("HIGH"));
    }

    @Test
    void shouldThrowExceptionForInvalidValueOf() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Priority.valueOf("INVALID")
        );
        assertEquals("No enum constant org.todobot.model.Priority.INVALID", exception.getMessage());
    }

    @Test
    void shouldParseValidPriorities() {
        assertEquals(Priority.HIGH, Priority.fromString("high"));
        assertEquals(Priority.HIGH, Priority.fromString("HIGH"));
        assertEquals(Priority.HIGH, Priority.fromString("h"));
        assertEquals(Priority.HIGH, Priority.fromString("H"));
        
        assertEquals(Priority.LOW, Priority.fromString("low"));
        assertEquals(Priority.LOW, Priority.fromString("LOW"));
        assertEquals(Priority.LOW, Priority.fromString("l"));
        assertEquals(Priority.LOW, Priority.fromString("L"));
        
        assertEquals(Priority.MEDIUM, Priority.fromString("medium"));
        assertEquals(Priority.MEDIUM, Priority.fromString("MEDIUM"));
        assertEquals(Priority.MEDIUM, Priority.fromString("m"));
        assertEquals(Priority.MEDIUM, Priority.fromString("M"));
    }

    @Test
    void shouldDefaultToMediumForInvalidInput() {
        assertEquals(Priority.MEDIUM, Priority.fromString("invalid"));
        assertEquals(Priority.MEDIUM, Priority.fromString(""));
        assertEquals(Priority.MEDIUM, Priority.fromString(null));
        assertEquals(Priority.MEDIUM, Priority.fromString("xyz"));
    }
}