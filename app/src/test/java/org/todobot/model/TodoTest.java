package org.todobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToDoTest {
    
    private ToDo todo;
    private static final String TEST_DESCRIPTION = "Read a book";
    
    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        todo = new ToDo(TEST_DESCRIPTION);
    }
    
    @Test
    void shouldCreateToDoWithDescription() {
        assertEquals(TEST_DESCRIPTION, todo.getDescription());
    }
    
    @Test
    void shouldInitiallyBeNotDone() {
        assertFalse(todo.isDone());
    }
    
    @Test
    void shouldMarkToDoAsDone() {
        todo.markAsDone();
        assertTrue(todo.isDone());
    }
    
    @Test
    void shouldMarkToDoAsNotDone() {
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
        String expected = "[T][ ][M] " + TEST_DESCRIPTION;
        assertEquals(expected, todo.toString());
    }
    
    @Test
    void shouldFormatToStringCorrectlyWhenDone() {
        todo.markAsDone();
        String expected = "[T][X][M] " + TEST_DESCRIPTION;
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
            () -> new ToDo(null)
        );
        assertEquals("Task description cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new ToDo("")
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForWhitespaceOnlyDescription() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new ToDo("   ")
        );
        assertEquals("Task description cannot be empty", exception.getMessage());
    }

    @Test
    void shouldHandleDescriptionWithSpaces() {
        String spaceyDescription = "Go to the   grocery    store";
        ToDo spaceyToDo = new ToDo(spaceyDescription);
        assertEquals(spaceyDescription, spaceyToDo.getDescription());
        assertEquals("[T][ ][M] " + spaceyDescription, spaceyToDo.toString());
    }
    
    @Test
    void shouldHandleSpecialCharacters() {
        String specialDescription = "Buy $100 worth of groceries @#$%^&*()";
        ToDo specialToDo = new ToDo(specialDescription);
        assertEquals(specialDescription, specialToDo.getDescription());
        assertEquals("[T][ ][M] " + specialDescription, specialToDo.toString());
    }
    
    @Test
    void shouldHandleVeryLongDescription() {
        String longDescription = "This is a very long task description that goes on and on and on because sometimes people write really long task descriptions that test the limits of our patience and string handling capabilities";
        ToDo longToDo = new ToDo(longDescription);
        assertEquals(longDescription, longToDo.getDescription());
        assertEquals("[T][ ][M] " + longDescription, longToDo.toString());
    }
    
    @Test
    void shouldHandleUnicodeCharacters() {
        String unicodeDescription = "Learn 中文 and العربية and русский";
        ToDo unicodeToDo = new ToDo(unicodeDescription);
        assertEquals(unicodeDescription, unicodeToDo.getDescription());
        assertEquals("[T][ ][M] " + unicodeDescription, unicodeToDo.toString());
    }
    
    @Test
    void shouldHandleDescriptionWithNewlines() {
        String newlineDescription = "Multi\nline\ndescription";
        ToDo newlineToDo = new ToDo(newlineDescription);
        assertEquals(newlineDescription, newlineToDo.getDescription());
        assertEquals("[T][ ][M] " + newlineDescription, newlineToDo.toString());
    }
}