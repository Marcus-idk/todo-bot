package org.todobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class TaskTypeIconTest {
    
    @Test
    void shouldReturnCorrectIconForTodo() {
        assertEquals("T", TaskTypeIcon.TODO.getIcon());
    }
    
    @Test
    void shouldReturnCorrectIconForDeadline() {
        assertEquals("D", TaskTypeIcon.DEADLINE.getIcon());
    }
    
    @Test
    void shouldReturnCorrectIconForEvent() {
        assertEquals("E", TaskTypeIcon.EVENT.getIcon());
    }
    
    @Test
    void shouldHaveCorrectNumberOfValues() {
        assertEquals(3, TaskTypeIcon.values().length);
    }
    
    @Test
    void shouldSupportValueOf() {
        assertEquals(TaskTypeIcon.TODO, TaskTypeIcon.valueOf("TODO"));
        assertEquals(TaskTypeIcon.DEADLINE, TaskTypeIcon.valueOf("DEADLINE"));
        assertEquals(TaskTypeIcon.EVENT, TaskTypeIcon.valueOf("EVENT"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidValueOf() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> TaskTypeIcon.valueOf("INVALID")
        );
        assertEquals("No enum constant org.todobot.model.TaskTypeIcon.INVALID", exception.getMessage());
    }
}