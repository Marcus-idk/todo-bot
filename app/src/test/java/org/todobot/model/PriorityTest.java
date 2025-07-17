package org.todobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class PriorityTest {
    
    @Test
    void shouldReturnCorrectIconForLow() {
        assertEquals("L", Priority.LOW.getIcon());
    }
    
    @Test
    void shouldReturnCorrectIconForMedium() {
        assertEquals("M", Priority.MEDIUM.getIcon());
    }
    
    @Test
    void shouldReturnCorrectIconForHigh() {
        assertEquals("H", Priority.HIGH.getIcon());
    }
    
    @Test
    void shouldHaveCorrectNumberOfValues() {
        assertEquals(3, Priority.values().length);
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
    void shouldMaintainCorrectOrder() {
        Priority[] priorities = Priority.values();
        assertEquals(Priority.LOW, priorities[0]);
        assertEquals(Priority.MEDIUM, priorities[1]);
        assertEquals(Priority.HIGH, priorities[2]);
    }
}