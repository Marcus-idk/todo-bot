package org.todobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.tasks.Todo;

public class TodoTest {
    
    private Todo todo;
    private static final String TEST_DESCRIPTION = "Read a book";
    
    @BeforeEach
    void setUp() {
        todo = new Todo(TEST_DESCRIPTION);
    }
    
    @Test
    void shouldCreateTodoWithDescription() {
        assertEquals(TEST_DESCRIPTION, todo.getDescription());
    }
    
    @Test
    void shouldInitiallyBeNotDone() {
        assertFalse(todo.isDone());
    }
    
    @Test
    void shouldMarkTodoAsDone() {
        todo.markAsDone();
        assertTrue(todo.isDone());
    }
    
    @Test
    void shouldMarkTodoAsNotDone() {
        todo.markAsDone();
        todo.markAsNotDone();
        assertFalse(todo.isDone());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenNotDone() {
        assertEquals(" ", todo.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectStatusIconWhenDone() {
        todo.markAsDone();
        assertEquals("X", todo.getStatusIcon());
    }
    
    @Test
    void shouldReturnCorrectTypeIcon() {
        assertEquals("T", todo.getTypeIcon());
    }
    
    @Test
    void shouldReturnEmptyDetailsString() {
        assertEquals("", todo.getDetailsString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenNotDone() {
        String expected = "[T][ ] " + TEST_DESCRIPTION;
        assertEquals(expected, todo.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenDone() {
        todo.markAsDone();
        String expected = "[T][X] " + TEST_DESCRIPTION;
        assertEquals(expected, todo.toString());
    }
    
    @Test
    void shouldReturnCorrectDescription() {
        assertEquals(TEST_DESCRIPTION, todo.getDescription());
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
    void shouldHandleDescriptionWithSpaces() {
        String spaceyDescription = "Go to the   grocery    store";
        Todo spaceyTodo = new Todo(spaceyDescription);
        assertEquals(spaceyDescription, spaceyTodo.getDescription());
        assertEquals("[T][ ] " + spaceyDescription, spaceyTodo.toString());
    }
    
    @Test
    void shouldHandleSpecialCharacters() {
        String specialDescription = "Buy $100 worth of groceries @#$%^&*()";
        Todo specialTodo = new Todo(specialDescription);
        assertEquals(specialDescription, specialTodo.getDescription());
        assertEquals("[T][ ] " + specialDescription, specialTodo.toString());
    }
    
    @Test
    void shouldHandleVeryLongDescription() {
        String longDescription = "This is a very long task description that goes on and on and on because sometimes people write really long task descriptions that test the limits of our patience and string handling capabilities";
        Todo longTodo = new Todo(longDescription);
        assertEquals(longDescription, longTodo.getDescription());
        assertEquals("[T][ ] " + longDescription, longTodo.toString());
    }
    
    @Test
    void shouldHandleUnicodeCharacters() {
        String unicodeDescription = "Learn 中文 and العربية and русский";
        Todo unicodeTodo = new Todo(unicodeDescription);
        assertEquals(unicodeDescription, unicodeTodo.getDescription());
        assertEquals("[T][ ] " + unicodeDescription, unicodeTodo.toString());
    }
    
    @Test
    void shouldHandleDescriptionWithNewlines() {
        String newlineDescription = "Multi\nline\ndescription";
        Todo newlineTodo = new Todo(newlineDescription);
        assertEquals(newlineDescription, newlineTodo.getDescription());
        assertEquals("[T][ ] " + newlineDescription, newlineTodo.toString());
    }
}