package org.todobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class TaskTest {
    
    private Task task;
    private static final String TEST_DESCRIPTION = "Read a book";
    
    @SuppressWarnings("unused")
    @BeforeEach
    void setUp() {
        task = new Task(TEST_DESCRIPTION);
    }
    
    @Test
    void shouldCreateTaskWithDescription() {
        assertEquals(TEST_DESCRIPTION, task.getDescription());
    }
    
    @Test
    void shouldInitiallyBeNotDone() {
        assertFalse(task.isDone());
    }
    
    @Test
    void shouldMarkTaskAsDone() {
        task.markAsDone();
        assertTrue(task.isDone());
    }
    
    @Test
    void shouldMarkTaskAsNotDone() {
        task.markAsDone();
        task.markAsNotDone();
        assertFalse(task.isDone());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenNotDone() {
        assertEquals(" ", task.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenDone() {
        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenNotDone() {
        String expected = "[ ] " + TEST_DESCRIPTION;
        assertEquals(expected, task.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenDone() {
        task.markAsDone();
        String expected = "[X] " + TEST_DESCRIPTION;
        assertEquals(expected, task.toString());
    }
    
    @Test
    void shouldReturnCorrectDescription() {
        assertEquals(TEST_DESCRIPTION, task.getDescription());
    }
    
    @Test
    void shouldHandleMultipleMarkUnmarkOperations() {
        // Test sequence: done -> undone -> done -> undone
        task.markAsDone();
        assertTrue(task.isDone());
        
        task.markAsNotDone();
        assertFalse(task.isDone());
        
        task.markAsDone();
        assertTrue(task.isDone());
        
        task.markAsNotDone();
        assertFalse(task.isDone());
    }
    
    @Test
    void shouldThrowExceptionForNullDescription() {
        // Verify that null description throws IllegalArgumentException
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Task(null)
        );
        assertEquals("Task description cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyDescription() {
        // Verify that empty string description throws IllegalArgumentException
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Task("")
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyDescription() {
        // Verify that whitespace-only description throws IllegalArgumentException
        // Example: "   " (three spaces)
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Task("   ")
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldHandleDescriptionWithSpaces() {
        // Verify that descriptions with multiple spaces are preserved correctly
        // Example: "Go to the   grocery    store" (extra spaces between words)
        String spaceyDescription = "Go to the   grocery    store";
        Task spaceyTask = new Task(spaceyDescription);
        assertEquals(spaceyDescription, spaceyTask.getDescription());
        assertEquals("[ ] " + spaceyDescription, spaceyTask.toString());
    }
    
    @Test
    void shouldHandleSpecialCharacters() {
        String specialDescription = "Buy $100 worth of groceries @#$%^&*()";
        Task specialTask = new Task(specialDescription);
        assertEquals(specialDescription, specialTask.getDescription());
        assertEquals("[ ] " + specialDescription, specialTask.toString());
    }
    
    @Test
    void shouldHandleVeryLongDescription() {
        String longDescription = "This is a very long task description that goes on and on and on because sometimes people write really long task descriptions that test the limits of our patience and string handling capabilities";
        Task longTask = new Task(longDescription);
        assertEquals(longDescription, longTask.getDescription());
        assertEquals("[ ] " + longDescription, longTask.toString());
    }
}