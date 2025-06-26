package org.todobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class TaskStatusIconTest {
    
    @Test
    void shouldReturnCorrectIconForDone() {
        assertEquals("X", TaskStatusIcon.DONE.getIcon());
    }
    
    @Test
    void shouldReturnCorrectIconForNotDone() {
        assertEquals(" ", TaskStatusIcon.NOT_DONE.getIcon());
    }
    
    @Test
    void shouldHaveCorrectNumberOfValues() {
        assertEquals(2, TaskStatusIcon.values().length);
    }
    
    @Test
    void shouldSupportValueOf() {
        assertEquals(TaskStatusIcon.DONE, TaskStatusIcon.valueOf("DONE"));
        assertEquals(TaskStatusIcon.NOT_DONE, TaskStatusIcon.valueOf("NOT_DONE"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidValueOf() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> TaskStatusIcon.valueOf("INVALID")
        );
        assertEquals("No enum constant org.todobot.model.TaskStatusIcon.INVALID", exception.getMessage());
    }
}