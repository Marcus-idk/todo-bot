package org.todobot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.model.Task;
import org.todobot.model.ToDo;

public class TaskListTest {
    
    private TaskList taskList;
    private static final String TEST_TASK_1 = "Read a book";
    private static final String TEST_TASK_2 = "Do homework";
    private static final String TEST_TASK_3 = "Buy groceries";
    
    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        taskList = new TaskList();
    }
    
    @Test
    void shouldInitializeEmptyTaskList() {
        assertTrue(taskList.isEmpty());
        assertFalse(taskList.isFull());
        assertEquals(0, taskList.getTaskCount());
    }
    
    @Test
    void shouldAddMultipleTasks() {
        assertTrue(taskList.addTask(new ToDo(TEST_TASK_1)));
        assertTrue(taskList.addTask(new ToDo(TEST_TASK_2)));
        assertTrue(taskList.addTask(new ToDo(TEST_TASK_3)));
        
        assertEquals(3, taskList.getTaskCount());
        assertFalse(taskList.isEmpty());
        assertFalse(taskList.isFull());
    }
    
    @Test
    void shouldRejectTaskWhenFull() {
        for (int i = 1; i <= 100; i++) {
            assertTrue(taskList.addTask(new ToDo("Task " + i)));
        }
        
        assertTrue(taskList.isFull());
        assertEquals(100, taskList.getTaskCount());
        
        assertFalse(taskList.addTask(new ToDo("Overflow task")));
        assertEquals(100, taskList.getTaskCount());
    }
    
    @Test
    void shouldReturnEmptyListMessage() {
        String expected = " Here are the tasks in your list:\n No tasks found! Your to-do list is completely empty.";
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldListMultipleTasks() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        taskList.addTask(new ToDo(TEST_TASK_3));
        
        String expected = " Here are the tasks in your list:\n" +
                         " 1.[T][ ][M] " + TEST_TASK_1 + "\n" +
                         " 2.[T][ ][M] " + TEST_TASK_2 + "\n" +
                         " 3.[T][ ][M] " + TEST_TASK_3;
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldListTasksWithMixedStatus() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        taskList.addTask(new ToDo(TEST_TASK_3));
        
        taskList.markTask(2);
        
        String expected = " Here are the tasks in your list:\n" +
                         " 1.[T][ ][M] " + TEST_TASK_1 + "\n" +
                         " 2.[T][X][M] " + TEST_TASK_2 + "\n" +
                         " 3.[T][ ][M] " + TEST_TASK_3;
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldRejectMarkingInvalidTaskNumber() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        
        assertFalse(taskList.markTask(0));
        assertFalse(taskList.markTask(-1));
        assertFalse(taskList.markTask(2));
        assertFalse(taskList.markTask(100));
    }
    
    @Test
    void shouldRejectMarkingEmptyList() {
        assertFalse(taskList.markTask(1));
    }
    
    @Test
    void shouldRejectUnmarkingInvalidTaskNumber() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        
        assertFalse(taskList.unmarkTask(0));
        assertFalse(taskList.unmarkTask(-1));
        assertFalse(taskList.unmarkTask(2));
        assertFalse(taskList.unmarkTask(100));
    }
    
    @Test
    void shouldRejectUnmarkingEmptyList() {
        assertFalse(taskList.unmarkTask(1));
    }
    
    @Test
    void shouldGetValidTask() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        
        Task task1 = taskList.getTask(1);
        Task task2 = taskList.getTask(2);
        
        assertEquals(TEST_TASK_1, task1.getDescription());
        assertEquals(TEST_TASK_2, task2.getDescription());
    }
    
    @Test
    void shouldReturnNullForInvalidTask() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        
        assertNull(taskList.getTask(0));
        assertNull(taskList.getTask(-1));
        assertNull(taskList.getTask(2));
        assertNull(taskList.getTask(100));
    }
    
    @Test
    void shouldDetectFullList() {
        assertFalse(taskList.isFull());
        
        for (int i = 1; i <= 100; i++) {
            taskList.addTask(new ToDo("Task " + i));
        }
        
        assertTrue(taskList.isFull());
    }
    
    @Test
    void shouldHandleMarkUnmarkSequence() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        
        assertFalse(taskList.getTask(1).isDone());
        
        assertTrue(taskList.markTask(1));
        assertTrue(taskList.getTask(1).isDone());
        
        assertTrue(taskList.unmarkTask(1));
        assertFalse(taskList.getTask(1).isDone());
        
        assertTrue(taskList.markTask(1));
        assertTrue(taskList.getTask(1).isDone());
    }
    
    @Test
    void shouldReturnNullForInvalidDeleteIndices() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        
        assertNull(taskList.deleteTask(0));
        assertNull(taskList.deleteTask(-1));
        assertNull(taskList.deleteTask(3));
        assertNull(taskList.deleteTask(100));
        
        assertEquals(2, taskList.getTaskCount());
    }
    
    @Test
    void shouldReturnNullForDeleteFromEmptyList() {
        assertNull(taskList.deleteTask(1));
        assertTrue(taskList.isEmpty());
    }
    
    @Test
    void shouldDeleteFirstTask() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        taskList.addTask(new ToDo(TEST_TASK_3));
        
        Task deletedTask = taskList.deleteTask(1);
        
        assertEquals(TEST_TASK_1, deletedTask.getDescription());
        assertEquals(2, taskList.getTaskCount());
        assertEquals(TEST_TASK_2, taskList.getTask(1).getDescription());
        assertEquals(TEST_TASK_3, taskList.getTask(2).getDescription());
    }
    
    @Test
    void shouldDeleteMiddleTask() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        taskList.addTask(new ToDo(TEST_TASK_3));
        
        Task deletedTask = taskList.deleteTask(2);
        
        assertEquals(TEST_TASK_2, deletedTask.getDescription());
        assertEquals(2, taskList.getTaskCount());
        assertEquals(TEST_TASK_1, taskList.getTask(1).getDescription());
        assertEquals(TEST_TASK_3, taskList.getTask(2).getDescription());
    }
    
    @Test
    void shouldDeleteLastTask() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        taskList.addTask(new ToDo(TEST_TASK_3));
        
        Task deletedTask = taskList.deleteTask(3);
        
        assertEquals(TEST_TASK_3, deletedTask.getDescription());
        assertEquals(2, taskList.getTaskCount());
        assertEquals(TEST_TASK_1, taskList.getTask(1).getDescription());
        assertEquals(TEST_TASK_2, taskList.getTask(2).getDescription());
        assertNull(taskList.getTask(3));
    }
    
    @Test
    void shouldDeleteOnlyTask() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        
        Task deletedTask = taskList.deleteTask(1);
        
        assertEquals(TEST_TASK_1, deletedTask.getDescription());
        assertEquals(0, taskList.getTaskCount());
        assertTrue(taskList.isEmpty());
        assertNull(taskList.getTask(1));
    }
    
    @Test
    void shouldHandleIndexShiftingAfterDeletion() {
        taskList.addTask(new ToDo("Task A"));
        taskList.addTask(new ToDo("Task B"));
        taskList.addTask(new ToDo("Task C"));
        taskList.addTask(new ToDo("Task D"));
        
        taskList.deleteTask(2);
        
        assertEquals("Task A", taskList.getTask(1).getDescription());
        assertEquals("Task C", taskList.getTask(2).getDescription());
        assertEquals("Task D", taskList.getTask(3).getDescription());
        assertNull(taskList.getTask(4));
        assertEquals(3, taskList.getTaskCount());
    }
    
    @Test
    void shouldDeleteMarkedTask() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        taskList.markTask(2);
        
        Task deletedTask = taskList.deleteTask(2);
        
        assertEquals(TEST_TASK_2, deletedTask.getDescription());
        assertTrue(deletedTask.isDone());
        assertEquals(1, taskList.getTaskCount());
        assertEquals(TEST_TASK_1, taskList.getTask(1).getDescription());
    }
    
    @Test
    void shouldHandleMultipleDeletions() {
        taskList.addTask(new ToDo("Task A"));
        taskList.addTask(new ToDo("Task B"));
        taskList.addTask(new ToDo("Task C"));
        taskList.addTask(new ToDo("Task D"));
        taskList.addTask(new ToDo("Task E"));
        
        taskList.deleteTask(2);
        taskList.deleteTask(2);
        
        assertEquals("Task A", taskList.getTask(1).getDescription());
        assertEquals("Task D", taskList.getTask(2).getDescription());
        assertEquals("Task E", taskList.getTask(3).getDescription());
        assertNull(taskList.getTask(4));
        assertEquals(3, taskList.getTaskCount());
    }
    
    @Test
    void shouldHandleDeleteThenAdd() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        
        taskList.deleteTask(1);
        taskList.addTask(new ToDo(TEST_TASK_3));
        
        assertEquals(2, taskList.getTaskCount());
        assertEquals(TEST_TASK_2, taskList.getTask(1).getDescription());
        assertEquals(TEST_TASK_3, taskList.getTask(2).getDescription());
    }
    
    @Test
    void shouldMaintainTaskOrderAfterDeletion() {
        taskList.addTask(new ToDo("First"));
        taskList.addTask(new ToDo("Second"));
        taskList.addTask(new ToDo("Third"));
        taskList.addTask(new ToDo("Fourth"));
        taskList.addTask(new ToDo("Fifth"));
        
        taskList.deleteTask(3);
        
        String expected = " Here are the tasks in your list:\n" +
                         " 1.[T][ ][M] First\n" +
                         " 2.[T][ ][M] Second\n" +
                         " 3.[T][ ][M] Fourth\n" +
                         " 4.[T][ ][M] Fifth";
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldFindMatchingTasks() {
        taskList.addTask(new ToDo("Read a book"));
        taskList.addTask(new ToDo("Do homework"));
        taskList.addTask(new ToDo("Buy book"));
        
        String result = taskList.findTasks("book");
        String expected = " Here are the matching tasks in your list:\n" +
                         " 1.[T][ ][M] Read a book\n" +
                         " 3.[T][ ][M] Buy book";
        assertEquals(expected, result);
    }
    
    @Test
    void shouldReturnNoMatchesWhenNotFound() {
        taskList.addTask(new ToDo("Read a book"));
        taskList.addTask(new ToDo("Do homework"));
        
        String result = taskList.findTasks("movie");
        String expected = " No matching tasks found for keyword: movie";
        assertEquals(expected, result);
    }
    
    @Test
    void shouldHandleEmptyList() {
        String result = taskList.findTasks("book");
        String expected = " No matching tasks found for keyword: book";
        assertEquals(expected, result);
    }
    
    @Test
    void shouldPerformCaseInsensitiveSearch() {
        taskList.addTask(new ToDo("Read a book"));
        taskList.addTask(new ToDo("Do homework"));
        
        String result = taskList.findTasks("BOOK");
        String expected = " Here are the matching tasks in your list:\n" +
                         " 1.[T][ ][M] Read a book";
        assertEquals(expected, result);
    }
    
    @Test
    void shouldHandleEmptyKeyword() {
        taskList.addTask(new ToDo("Read a book"));
        
        String result = taskList.findTasks("");
        String expected = " Please provide a keyword to search for.";
        assertEquals(expected, result);
        
        String resultNull = taskList.findTasks(null);
        assertEquals(expected, resultNull);
    }
    
    @Test
    void shouldMaintainOriginalTaskNumbers() {
        taskList.addTask(new ToDo("Task A"));
        taskList.addTask(new ToDo("Task B"));
        taskList.addTask(new ToDo("Find this task"));
        taskList.addTask(new ToDo("Task D"));
        taskList.addTask(new ToDo("Find this too"));
        
        String result = taskList.findTasks("Find");
        String expected = " Here are the matching tasks in your list:\n" +
                         " 3.[T][ ][M] Find this task\n" +
                         " 5.[T][ ][M] Find this too";
        assertEquals(expected, result);
    }
    
    @Test
    void shouldDeleteAllTasks() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        taskList.addTask(new ToDo(TEST_TASK_3));
        
        assertEquals(3, taskList.getTaskCount());
        assertFalse(taskList.isEmpty());
        
        int deletedCount = taskList.deleteAllTasks();
        
        assertEquals(3, deletedCount);
        assertEquals(0, taskList.getTaskCount());
        assertTrue(taskList.isEmpty());
        assertNull(taskList.getTask(1));
    }
    
    @Test
    void shouldDeleteAllFromEmptyList() {
        assertEquals(0, taskList.deleteAllTasks());
        assertTrue(taskList.isEmpty());
    }
    
    @Test
    void shouldGetAllTasks() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        taskList.addTask(new ToDo(TEST_TASK_2));
        
        ArrayList<Task> allTasks = taskList.getAllTasks();
        
        assertEquals(2, allTasks.size());
        assertEquals(TEST_TASK_1, allTasks.get(0).getDescription());
        assertEquals(TEST_TASK_2, allTasks.get(1).getDescription());
        
        // Verify it's a defensive copy - modifying returned list shouldn't affect original
        allTasks.clear();
        assertEquals(2, taskList.getTaskCount());
        assertEquals(TEST_TASK_1, taskList.getTask(1).getDescription());
    }
    
    @Test
    void shouldSetTasks() {
        // Add initial tasks
        taskList.addTask(new ToDo("Original task"));
        assertEquals(1, taskList.getTaskCount());
        
        // Create new task list
        ArrayList<Task> newTasks = new ArrayList<>();
        newTasks.add(new ToDo(TEST_TASK_1));
        newTasks.add(new ToDo(TEST_TASK_2));
        
        taskList.setTasks(newTasks);
        
        assertEquals(2, taskList.getTaskCount());
        assertEquals(TEST_TASK_1, taskList.getTask(1).getDescription());
        assertEquals(TEST_TASK_2, taskList.getTask(2).getDescription());
        assertNull(taskList.getTask(3));
    }
    
    @Test
    void shouldSetEmptyTaskList() {
        taskList.addTask(new ToDo(TEST_TASK_1));
        assertEquals(1, taskList.getTaskCount());
        
        taskList.setTasks(new ArrayList<>());
        
        assertEquals(0, taskList.getTaskCount());
        assertTrue(taskList.isEmpty());
    }
}