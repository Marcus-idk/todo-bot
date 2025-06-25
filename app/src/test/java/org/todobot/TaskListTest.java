package org.todobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.model.Task;
import org.todobot.model.Todo;
import org.todobot.service.TaskList;

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
        assertTrue(taskList.addTask(new Todo(TEST_TASK_1)));
        assertFalse(taskList.isEmpty());
        assertEquals(1, taskList.getTaskCount());
    }
    
    @Test
    void shouldAddMultipleTasks() {
        assertTrue(taskList.addTask(new Todo(TEST_TASK_1)));
        assertTrue(taskList.addTask(new Todo(TEST_TASK_2)));
        assertTrue(taskList.addTask(new Todo(TEST_TASK_3)));
        
        assertEquals(3, taskList.getTaskCount());
        assertFalse(taskList.isEmpty());
        assertFalse(taskList.isFull());
    }
    
    @Test
    void shouldRejectTaskWhenFull() {
        for (int i = 1; i <= 100; i++) {
            assertTrue(taskList.addTask(new Todo("Task " + i)));
        }
        
        assertTrue(taskList.isFull());
        assertEquals(100, taskList.getTaskCount());
        
        assertFalse(taskList.addTask(new Todo("Overflow task")));
        assertEquals(100, taskList.getTaskCount());
    }
    
    @Test
    void shouldReturnEmptyListMessage() {
        String expected = " Here are the tasks in your list:\n No tasks found! Your to-do list is as empty as my brain! 🤖";
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldListSingleTask() {
        taskList.addTask(new Todo(TEST_TASK_1));
        String expected = " Here are the tasks in your list:\n 1.[T][ ] " + TEST_TASK_1;
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldListMultipleTasks() {
        taskList.addTask(new Todo(TEST_TASK_1));
        taskList.addTask(new Todo(TEST_TASK_2));
        taskList.addTask(new Todo(TEST_TASK_3));
        
        String expected = " Here are the tasks in your list:\n" +
                         " 1.[T][ ] " + TEST_TASK_1 + "\n" +
                         " 2.[T][ ] " + TEST_TASK_2 + "\n" +
                         " 3.[T][ ] " + TEST_TASK_3;
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldListTasksWithMixedStatus() {
        taskList.addTask(new Todo(TEST_TASK_1));
        taskList.addTask(new Todo(TEST_TASK_2));
        taskList.addTask(new Todo(TEST_TASK_3));
        
        taskList.markTask(2);
        
        String expected = " Here are the tasks in your list:\n" +
                         " 1.[T][ ] " + TEST_TASK_1 + "\n" +
                         " 2.[T][X] " + TEST_TASK_2 + "\n" +
                         " 3.[T][ ] " + TEST_TASK_3;
        assertEquals(expected, taskList.listTasks());
    }
    
    @Test
    void shouldMarkValidTask() {
        taskList.addTask(new Todo(TEST_TASK_1));
        assertTrue(taskList.markTask(1));
        
        Task task = taskList.getTask(1);
        assertTrue(task.isDone());
    }
    
    @Test
    void shouldRejectMarkingInvalidTaskNumber() {
        taskList.addTask(new Todo(TEST_TASK_1));
        
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
    void shouldUnmarkValidTask() {
        taskList.addTask(new Todo(TEST_TASK_1));
        taskList.markTask(1);
        assertTrue(taskList.unmarkTask(1));
        
        Task task = taskList.getTask(1);
        assertFalse(task.isDone());
    }
    
    @Test
    void shouldRejectUnmarkingInvalidTaskNumber() {
        taskList.addTask(new Todo(TEST_TASK_1));
        
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
        taskList.addTask(new Todo(TEST_TASK_1));
        taskList.addTask(new Todo(TEST_TASK_2));
        
        Task task1 = taskList.getTask(1);
        Task task2 = taskList.getTask(2);
        
        assertEquals(TEST_TASK_1, task1.getDescription());
        assertEquals(TEST_TASK_2, task2.getDescription());
    }
    
    @Test
    void shouldReturnNullForInvalidTask() {
        taskList.addTask(new Todo(TEST_TASK_1));
        
        assertNull(taskList.getTask(0));
        assertNull(taskList.getTask(-1));
        assertNull(taskList.getTask(2));
        assertNull(taskList.getTask(100));
    }
    
    @Test
    void shouldReturnNullForEmptyList() {
        assertNull(taskList.getTask(1));
    }
    
    @Test
    void shouldTrackTaskCount() {
        assertEquals(0, taskList.getTaskCount());
        
        taskList.addTask(new Todo(TEST_TASK_1));
        assertEquals(1, taskList.getTaskCount());
        
        taskList.addTask(new Todo(TEST_TASK_2));
        assertEquals(2, taskList.getTaskCount());
        
        taskList.addTask(new Todo(TEST_TASK_3));
        assertEquals(3, taskList.getTaskCount());
    }
    
    @Test
    void shouldDetectFullList() {
        assertFalse(taskList.isFull());
        
        for (int i = 1; i <= 100; i++) {
            taskList.addTask(new Todo("Task " + i));
        }
        
        assertTrue(taskList.isFull());
    }
    
    @Test
    void shouldDetectEmptyList() {
        assertTrue(taskList.isEmpty());
        
        taskList.addTask(new Todo(TEST_TASK_1));
        assertFalse(taskList.isEmpty());
    }
    
    @Test
    void shouldHandleTaskNumberingCorrectly() {
        taskList.addTask(new Todo("First task"));
        taskList.addTask(new Todo("Second task"));
        taskList.addTask(new Todo("Third task"));
        
        assertEquals("First task", taskList.getTask(1).getDescription());
        assertEquals("Second task", taskList.getTask(2).getDescription());
        assertEquals("Third task", taskList.getTask(3).getDescription());
        
        assertNull(taskList.getTask(0));
    }
    
    @Test
    void shouldHandleMarkUnmarkSequence() {
        taskList.addTask(new Todo(TEST_TASK_1));
        
        assertFalse(taskList.getTask(1).isDone());
        
        assertTrue(taskList.markTask(1));
        assertTrue(taskList.getTask(1).isDone());
        
        assertTrue(taskList.unmarkTask(1));
        assertFalse(taskList.getTask(1).isDone());
        
        assertTrue(taskList.markTask(1));
        assertTrue(taskList.getTask(1).isDone());
    }
}