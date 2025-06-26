package org.todobot.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {
    
    private Todo todo;
    private Deadline deadline;
    private Event event;
    private static final String TEST_DESCRIPTION = "Test task";
    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2019, 12, 25, 14, 30);
    
    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        todo = new Todo(TEST_DESCRIPTION);
        deadline = new Deadline(TEST_DESCRIPTION, TEST_DATE, false);
        event = new Event(TEST_DESCRIPTION, TEST_DATE, false, TEST_DATE, false);
    }
    
    @Test
    void shouldThrowExceptionForNullDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Todo(null)
        );
        assertEquals("Task description cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Todo("")
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Todo("   ")
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }
    
    @Test
    void shouldInitiallyBeNotDone() {
        assertFalse(todo.isDone());
        assertFalse(deadline.isDone());
        assertFalse(event.isDone());
    }
    
    @Test
    void shouldMarkTaskAsDone() {
        todo.markAsDone();
        assertTrue(todo.isDone());
    }
    
    @Test
    void shouldMarkTaskAsNotDone() {
        todo.markAsDone();
        todo.markAsNotDone();
        assertFalse(todo.isDone());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenNotDone() {
        assertEquals(" ", todo.getStatusIcon());
        assertEquals(" ", deadline.getStatusIcon());
        assertEquals(" ", event.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenDone() {
        todo.markAsDone();
        deadline.markAsDone();
        event.markAsDone();
        assertEquals("X", todo.getStatusIcon());
        assertEquals("X", deadline.getStatusIcon());
        assertEquals("X", event.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectDescription() {
        assertEquals(TEST_DESCRIPTION, todo.getDescription());
        assertEquals(TEST_DESCRIPTION, deadline.getDescription());
        assertEquals(TEST_DESCRIPTION, event.getDescription());
    }
    
    @Test
    void shouldHandleMultipleMarkUnmarkOperations() {
        todo.markAsDone();
        assertTrue(todo.isDone());
        
        todo.markAsNotDone();
        assertFalse(todo.isDone());
        
        todo.markAsDone();
        assertTrue(todo.isDone());
        
        todo.markAsNotDone();
        assertFalse(todo.isDone());
    }
    
    @Test
    void shouldHandleDescriptionWithSpaces() {
        String spaceyDescription = "Task with   multiple   spaces";
        Todo spaceyTodo = new Todo(spaceyDescription);
        assertEquals(spaceyDescription, spaceyTodo.getDescription());
    }
    
    @Test
    void shouldHandleSpecialCharactersInDescription() {
        String specialDescription = "Task with @#$%^&*() symbols!";
        Todo specialTodo = new Todo(specialDescription);
        assertEquals(specialDescription, specialTodo.getDescription());
    }
    
    @Test
    void shouldHandleVeryLongDescription() {
        String longDescription = "This is a very long task description that goes on and on to test string handling capabilities";
        Todo longTodo = new Todo(longDescription);
        assertEquals(longDescription, longTodo.getDescription());
    }
    
    @Test
    void shouldSupportPolymorphismWithTaskReference() {
        Task task = new Todo("Polymorphic todo");
        assertEquals("T", task.getTypeIcon());
        assertEquals("", task.getDetailsString());
        assertEquals("[T][ ] Polymorphic todo", task.toString());
    }
    
    @Test
    void shouldMaintainPolymorphicBehaviorForAllSubtypes() {
        Task todoTask = new Todo("Todo task");
        Task deadlineTask = new Deadline("Deadline task", TEST_DATE, false);
        Task eventTask = new Event("Event task", TEST_DATE, false, TEST_DATE, false);
        
        assertEquals("T", todoTask.getTypeIcon());
        assertEquals("D", deadlineTask.getTypeIcon());
        assertEquals("E", eventTask.getTypeIcon());
    }
}