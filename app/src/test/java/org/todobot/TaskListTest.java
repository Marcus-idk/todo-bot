package org.todobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void shouldAddSingleTask() {
        assertTrue(taskList.addTask(TEST_TASK_1));
        assertFalse(taskList.isEmpty());
        assertEquals(1, taskList.getTaskCount());
    }
    
    @Test
    void shouldAddMultipleTasks() {
        assertTrue(taskList.addTask(TEST_TASK_1));
        assertTrue(taskList.addTask(TEST_TASK_2));
        assertTrue(taskList.addTask(TEST_TASK_3));
        
        assertEquals(3, taskList.getTaskCount());
        assertFalse(taskList.isEmpty());
        assertFalse(taskList.isFull());
    }
    
    @Test
    void shouldRejectTaskWhenFull() {
        // Fill up the task list to capacity (100 tasks)
        for (int i = 1; i <= 100; i++) {
            assertTrue(taskList.addTask("Task " + i));
        }
        
        assertTrue(taskList.isFull());
        assertEquals(100, taskList.getTaskCount());
        
        // Try to add one more task - should fail
        assertFalse(taskList.addTask("Overflow task"));
        assertEquals(100, taskList.getTaskCount());
    }
    
    @Test
    void shouldReturnEmptyListMessage() {
        String expected = "No tasks found! Your to-do list is as empty as my brain! 🤖";
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldListSingleTask() {
        taskList.addTask(TEST_TASK_1);
        String expected = " 1. [ ] " + TEST_TASK_1;
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldListMultipleTasks() {
        taskList.addTask(TEST_TASK_1);
        taskList.addTask(TEST_TASK_2);
        taskList.addTask(TEST_TASK_3);
        
        String expected = " 1. [ ] " + TEST_TASK_1 + "\n" +
                         " 2. [ ] " + TEST_TASK_2 + "\n" +
                         " 3. [ ] " + TEST_TASK_3;
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldListTasksWithMixedStatus() {
        taskList.addTask(TEST_TASK_1);
        taskList.addTask(TEST_TASK_2);
        taskList.addTask(TEST_TASK_3);
        
        // Mark second task as done
        taskList.markTask(2);
        
        String expected = " 1. [ ] " + TEST_TASK_1 + "\n" +
                         " 2. [X] " + TEST_TASK_2 + "\n" +
                         " 3. [ ] " + TEST_TASK_3;
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldMarkValidTask() {
        taskList.addTask(TEST_TASK_1);
        assertTrue(taskList.markTask(1));
        
        Task task = taskList.getTask(1);
        assertTrue(task.isDone());
    }
    
    @Test
    void shouldRejectMarkingInvalidTaskNumber() {
        taskList.addTask(TEST_TASK_1);
        
        // Test invalid task numbers
        assertFalse(taskList.markTask(0));      // Below range
        assertFalse(taskList.markTask(-1));     // Negative
        assertFalse(taskList.markTask(2));      // Above range
        assertFalse(taskList.markTask(100));    // Way above range
    }
    
    @Test
    void shouldRejectMarkingEmptyList() {
        assertFalse(taskList.markTask(1));
    }
    
    @Test
    void shouldUnmarkValidTask() {
        taskList.addTask(TEST_TASK_1);
        taskList.markTask(1);  // Mark it first
        assertTrue(taskList.unmarkTask(1));
        
        Task task = taskList.getTask(1);
        assertFalse(task.isDone());
    }
    
    @Test
    void shouldRejectUnmarkingInvalidTaskNumber() {
        taskList.addTask(TEST_TASK_1);
        
        // Test invalid task numbers
        assertFalse(taskList.unmarkTask(0));      // Below range
        assertFalse(taskList.unmarkTask(-1));     // Negative
        assertFalse(taskList.unmarkTask(2));      // Above range
        assertFalse(taskList.unmarkTask(100));    // Way above range
    }
    
    @Test
    void shouldRejectUnmarkingEmptyList() {
        assertFalse(taskList.unmarkTask(1));
    }
    
    @Test
    void shouldGetValidTask() {
        taskList.addTask(TEST_TASK_1);
        taskList.addTask(TEST_TASK_2);
        
        Task task1 = taskList.getTask(1);
        Task task2 = taskList.getTask(2);
        
        assertEquals(TEST_TASK_1, task1.getDescription());
        assertEquals(TEST_TASK_2, task2.getDescription());
    }
    
    @Test
    void shouldReturnNullForInvalidTask() {
        taskList.addTask(TEST_TASK_1);
        
        assertNull(taskList.getTask(0));      // Below range
        assertNull(taskList.getTask(-1));     // Negative
        assertNull(taskList.getTask(2));      // Above range
        assertNull(taskList.getTask(100));    // Way above range
    }
    
    @Test
    void shouldReturnNullForEmptyList() {
        assertNull(taskList.getTask(1));
    }
    
    @Test
    void shouldTrackTaskCount() {
        assertEquals(0, taskList.getTaskCount());
        
        taskList.addTask(TEST_TASK_1);
        assertEquals(1, taskList.getTaskCount());
        
        taskList.addTask(TEST_TASK_2);
        assertEquals(2, taskList.getTaskCount());
        
        taskList.addTask(TEST_TASK_3);
        assertEquals(3, taskList.getTaskCount());
    }
    
    @Test
    void shouldDetectFullList() {
        assertFalse(taskList.isFull());
        
        // Add 100 tasks
        for (int i = 1; i <= 100; i++) {
            taskList.addTask("Task " + i);
        }
        
        assertTrue(taskList.isFull());
    }
    
    @Test
    void shouldDetectEmptyList() {
        assertTrue(taskList.isEmpty());
        
        taskList.addTask(TEST_TASK_1);
        assertFalse(taskList.isEmpty());
    }
    
    @Test
    void shouldHandleTaskNumberingCorrectly() {
        // Verify that tasks are numbered starting from 1, not 0
        taskList.addTask("First task");
        taskList.addTask("Second task");
        taskList.addTask("Third task");
        
        // Should be able to access tasks 1, 2, 3
        assertEquals("First task", taskList.getTask(1).getDescription());
        assertEquals("Second task", taskList.getTask(2).getDescription());
        assertEquals("Third task", taskList.getTask(3).getDescription());
        
        // Task 0 should not exist
        assertNull(taskList.getTask(0));
    }
    
    @Test
    void shouldHandleMarkUnmarkSequence() {
        taskList.addTask(TEST_TASK_1);
        
        // Initially not done
        assertFalse(taskList.getTask(1).isDone());
        
        // Mark as done
        assertTrue(taskList.markTask(1));
        assertTrue(taskList.getTask(1).isDone());
        
        // Unmark
        assertTrue(taskList.unmarkTask(1));
        assertFalse(taskList.getTask(1).isDone());
        
        // Mark again
        assertTrue(taskList.markTask(1));
        assertTrue(taskList.getTask(1).isDone());
    }
}