package org.todobot.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.todobot.model.Deadline;
import org.todobot.model.Event;
import org.todobot.model.Task;
import org.todobot.model.ToDo;

public class TaskStorageTest {
    
    @TempDir
    Path tempDir;
    
    private TaskStorage taskStorage;
    private Path testFilePath;
    
    private static final String TEST_TODO_DESC = "Read a book";
    private static final String TEST_DEADLINE_DESC = "Submit report";
    private static final String TEST_EVENT_DESC = "Team meeting";
    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2024, 12, 25, 14, 30);
    private static final LocalDateTime TEST_DATE_2 = LocalDateTime.of(2024, 12, 25, 16, 0);
    
    @BeforeEach
    void setUp() {
        taskStorage = new TaskStorage(tempDir.toString());
        testFilePath = tempDir.resolve("KunBot.txt");
    }
    
    // 1. Save/Load Happy Path Tests
    
    @Test
    void shouldSaveAndLoadEmptyTaskList() {
        ArrayList<Task> emptyList = new ArrayList<>();
        
        taskStorage.saveTasks(emptyList);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertTrue(loadedTasks.isEmpty());
    }
    
    @Test
    void shouldSaveAndLoadSingleTodo() {
        ArrayList<Task> tasks = new ArrayList<>();
        ToDo todo = new ToDo(TEST_TODO_DESC);
        tasks.add(todo);
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(1, loadedTasks.size());
        Task loadedTask = loadedTasks.get(0);
        assertEquals(TEST_TODO_DESC, loadedTask.getDescription());
        assertFalse(loadedTask.isDone());
        assertTrue(loadedTask instanceof ToDo);
    }
    
    @Test
    void shouldSaveAndLoadMultipleTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new ToDo(TEST_TODO_DESC));
        tasks.add(new Deadline(TEST_DEADLINE_DESC, TEST_DATE, true));
        tasks.add(new Event(TEST_EVENT_DESC, TEST_DATE, true, TEST_DATE_2, true));
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(3, loadedTasks.size());
        assertTrue(loadedTasks.get(0) instanceof ToDo);
        assertTrue(loadedTasks.get(1) instanceof Deadline);
        assertTrue(loadedTasks.get(2) instanceof Event);
    }
    
    @Test
    void shouldSaveAndLoadTasksWithCorrectStatus() {
        ArrayList<Task> tasks = new ArrayList<>();
        ToDo todo = new ToDo(TEST_TODO_DESC);
        todo.markAsDone();
        tasks.add(todo);
        
        ToDo todo2 = new ToDo("Another task");
        tasks.add(todo2);
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(2, loadedTasks.size());
        assertTrue(loadedTasks.get(0).isDone());
        assertFalse(loadedTasks.get(1).isDone());
    }
    
    // 2. Task Type Serialization Tests
    
    @Test
    void shouldSaveAndLoadTodoTask() {
        ArrayList<Task> tasks = new ArrayList<>();
        ToDo todo = new ToDo("Test todo task");
        todo.markAsDone();
        tasks.add(todo);
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(1, loadedTasks.size());
        Task loadedTask = loadedTasks.get(0);
        assertTrue(loadedTask instanceof ToDo);
        assertEquals("Test todo task", loadedTask.getDescription());
        assertTrue(loadedTask.isDone());
    }
    
    @Test
    void shouldSaveAndLoadDeadlineTask() {
        ArrayList<Task> tasks = new ArrayList<>();
        Deadline deadline = new Deadline(TEST_DEADLINE_DESC, TEST_DATE, true);
        tasks.add(deadline);
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(1, loadedTasks.size());
        Task loadedTask = loadedTasks.get(0);
        assertTrue(loadedTask instanceof Deadline);
        Deadline loadedDeadline = (Deadline) loadedTask;
        assertEquals(TEST_DEADLINE_DESC, loadedDeadline.getDescription());
        assertEquals(TEST_DATE, loadedDeadline.getByDateTime());
        assertTrue(loadedDeadline.hasTimeInfo());
    }
    
    @Test
    void shouldSaveAndLoadEventTask() {
        ArrayList<Task> tasks = new ArrayList<>();
        Event event = new Event(TEST_EVENT_DESC, TEST_DATE, true, TEST_DATE_2, true);
        tasks.add(event);
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(1, loadedTasks.size());
        Task loadedTask = loadedTasks.get(0);
        assertTrue(loadedTask instanceof Event);
        Event loadedEvent = (Event) loadedTask;
        assertEquals(TEST_EVENT_DESC, loadedEvent.getDescription());
        assertEquals(TEST_DATE, loadedEvent.getFromDateTime());
        assertEquals(TEST_DATE_2, loadedEvent.getToDateTime());
        assertTrue(loadedEvent.hasFromTime());
        assertTrue(loadedEvent.hasToTime());
    }
    
    // 3. File System Edge Cases
    
    @Test
    void shouldCreateDataDirectoryIfMissing() {
        // TempDir already exists, but test file doesn't
        assertFalse(Files.exists(testFilePath));
        
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new ToDo(TEST_TODO_DESC));
        
        taskStorage.saveTasks(tasks);
        
        assertTrue(Files.exists(testFilePath));
    }
    
    @Test
    void shouldHandleMissingFile() {
        assertFalse(Files.exists(testFilePath));
        
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertTrue(loadedTasks.isEmpty());
    }
    
    @Test
    void shouldHandleEmptyFile() throws IOException {
        Files.writeString(testFilePath, "");
        
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertTrue(loadedTasks.isEmpty());
    }
    
    @Test
    void shouldHandleCorruptedJsonFile() throws IOException {
        Files.writeString(testFilePath, "invalid json content {[}");
        
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertTrue(loadedTasks.isEmpty());
    }
    
    // 4. Error Handling Tests
    
    @Test
    void shouldHandleUnknownTaskType() throws IOException {
        String invalidJson = "[{\"type\":\"UnknownTask\",\"description\":\"test\",\"isDone\":false}]";
        Files.writeString(testFilePath, invalidJson);
        
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertTrue(loadedTasks.isEmpty());
    }
    
    @Test
    void shouldHandleMalformedTaskData() throws IOException {
        String malformedJson = "[{\"type\":\"ToDo\",\"isDone\":false}]"; // missing description
        Files.writeString(testFilePath, malformedJson);
        
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertTrue(loadedTasks.isEmpty());
    }
    
    @Test
    void shouldHandleInvalidDateFormat() throws IOException {
        String invalidDateJson = "[{\"type\":\"Deadline\",\"description\":\"test\",\"isDone\":false,\"by\":\"invalid-date\",\"hasTime\":true}]";
        Files.writeString(testFilePath, invalidDateJson);
        
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertTrue(loadedTasks.isEmpty());
    }
    
    @Test
    void shouldSkipCorruptedTasksButLoadValidOnes() throws IOException {
        String mixedJson = "[" +
            "{\"type\":\"ToDo\",\"description\":\"Valid task\",\"isDone\":false}," +
            "{\"type\":\"UnknownTask\",\"description\":\"Invalid task\",\"isDone\":false}," +
            "{\"type\":\"ToDo\",\"description\":\"Another valid task\",\"isDone\":true}" +
            "]";
        Files.writeString(testFilePath, mixedJson);
        
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(2, loadedTasks.size());
        assertEquals("Valid task", loadedTasks.get(0).getDescription());
        assertFalse(loadedTasks.get(0).isDone());
        assertEquals("Another valid task", loadedTasks.get(1).getDescription());
        assertTrue(loadedTasks.get(1).isDone());
    }
    
    // 5. Data Integrity Tests
    
    @Test
    void shouldPreserveTaskOrder() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new ToDo("First task"));
        tasks.add(new ToDo("Second task"));
        tasks.add(new ToDo("Third task"));
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(3, loadedTasks.size());
        assertEquals("First task", loadedTasks.get(0).getDescription());
        assertEquals("Second task", loadedTasks.get(1).getDescription());
        assertEquals("Third task", loadedTasks.get(2).getDescription());
    }
    
    @Test
    void shouldPreserveAllTaskProperties() {
        ArrayList<Task> tasks = new ArrayList<>();
        
        Deadline deadline = new Deadline("Important deadline", TEST_DATE, false);
        deadline.markAsDone();
        tasks.add(deadline);
        
        Event event = new Event("Conference", TEST_DATE, true, TEST_DATE_2, false);
        tasks.add(event);
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(2, loadedTasks.size());
        
        Deadline loadedDeadline = (Deadline) loadedTasks.get(0);
        assertTrue(loadedDeadline.isDone());
        assertEquals("Important deadline", loadedDeadline.getDescription());
        assertEquals(TEST_DATE, loadedDeadline.getByDateTime());
        assertFalse(loadedDeadline.hasTimeInfo());
        
        Event loadedEvent = (Event) loadedTasks.get(1);
        assertFalse(loadedEvent.isDone());
        assertEquals("Conference", loadedEvent.getDescription());
        assertEquals(TEST_DATE, loadedEvent.getFromDateTime());
        assertEquals(TEST_DATE_2, loadedEvent.getToDateTime());
        assertTrue(loadedEvent.hasFromTime());
        assertFalse(loadedEvent.hasToTime());
    }
    
    @Test
    void shouldHandleSpecialCharactersInDescription() {
        ArrayList<Task> tasks = new ArrayList<>();
        String specialDesc = "Task with \"quotes\", [brackets], {braces}, and symbols: @#$%^&*()";
        tasks.add(new ToDo(specialDesc));
        
        taskStorage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(1, loadedTasks.size());
        assertEquals(specialDesc, loadedTasks.get(0).getDescription());
    }
    
    // 6. File Operations Tests
    
    @Test
    void shouldOverwriteExistingFile() {
        // First save
        ArrayList<Task> tasks1 = new ArrayList<>();
        tasks1.add(new ToDo("Original task"));
        taskStorage.saveTasks(tasks1);
        
        // Second save should overwrite
        ArrayList<Task> tasks2 = new ArrayList<>();
        tasks2.add(new ToDo("New task"));
        tasks2.add(new ToDo("Another new task"));
        taskStorage.saveTasks(tasks2);
        
        ArrayList<Task> loadedTasks = taskStorage.loadTasks();
        
        assertEquals(2, loadedTasks.size());
        assertEquals("New task", loadedTasks.get(0).getDescription());
        assertEquals("Another new task", loadedTasks.get(1).getDescription());
    }
}