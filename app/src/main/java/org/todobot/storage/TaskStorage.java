package org.todobot.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.todobot.model.Deadline;
import org.todobot.model.Event;
import org.todobot.model.Task;
import org.todobot.model.ToDo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class TaskStorage {
    private static final String DATA_DIR = "./data";
    private static final String FILE_NAME = "KunBot.txt";
    private static final Path FILE_PATH = Paths.get(DATA_DIR, FILE_NAME);
    
    private final Gson gson;
    
    public TaskStorage() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }
    
    public ArrayList<Task> loadTasks() {
        try {
            String json = readJsonContent();
            if (json == null) {
                return new ArrayList<>();
            }
            
            JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
            return jsonArrayToTasks(jsonArray);
            
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void saveTasks(ArrayList<Task> tasks) {
        try {
            JsonArray jsonArray = tasksToJsonArray(tasks);
            String json = gson.toJson(jsonArray);
            writeJsonToFile(json);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    private String readJsonContent() throws IOException {
        if (!Files.exists(FILE_PATH)) {
            return null;
        }
        
        String json = Files.readString(FILE_PATH);
        return json.trim().isEmpty() ? null : json;
    }
    
    private JsonObject taskToJsonObject(Task task) {
        JsonObject taskObj = new JsonObject();
        taskObj.addProperty("type", task.getClass().getSimpleName());
        taskObj.addProperty("description", task.getDescription());
        taskObj.addProperty("isDone", task.isDone());
        
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            taskObj.addProperty("by", deadline.getByDateTime().toString());
            taskObj.addProperty("hasTime", deadline.hasTimeInfo());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            taskObj.addProperty("from", event.getFromDateTime().toString());
            taskObj.addProperty("hasFromTime", event.hasFromTime());
            taskObj.addProperty("to", event.getToDateTime().toString());
            taskObj.addProperty("hasToTime", event.hasToTime());
        }
        
        return taskObj;
    }
    
    private JsonArray tasksToJsonArray(ArrayList<Task> tasks) {
        JsonArray jsonArray = new JsonArray();
        for (Task task : tasks) {
            jsonArray.add(taskToJsonObject(task));
        }
        return jsonArray;
    }
    
    private ArrayList<Task> jsonArrayToTasks(JsonArray jsonArray) {
        ArrayList<Task> tasks = new ArrayList<>();
        
        for (JsonElement element : jsonArray) {
            JsonObject taskObj = element.getAsJsonObject();
            Task task = parseTask(taskObj);
            if (task != null) {
                tasks.add(task);
            }
        }
        
        return tasks;
    }
    
    private void writeJsonToFile(String json) throws IOException {
        createDataDirectoryIfNeeded();
        Files.writeString(FILE_PATH, json);
    }
    
    private void createDataDirectoryIfNeeded() throws IOException {
        Path dataDir = Paths.get(DATA_DIR);
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }
    }
    
    private Task parseTask(JsonObject taskObj) {
        try {
            String type = taskObj.get("type").getAsString();
            String description = taskObj.get("description").getAsString();
            boolean isDone = taskObj.get("isDone").getAsBoolean();
            
            Task task;
            switch (type) {
                case "ToDo":
                    task = new ToDo(description);
                    break;
                case "Deadline":
                    String byStr = taskObj.get("by").getAsString();
                    boolean hasTime = taskObj.get("hasTime").getAsBoolean();
                    LocalDateTime by = LocalDateTime.parse(byStr);
                    task = new Deadline(description, by, hasTime);
                    break;
                case "Event":
                    String fromStr = taskObj.get("from").getAsString();
                    boolean hasFromTime = taskObj.get("hasFromTime").getAsBoolean();
                    String toStr = taskObj.get("to").getAsString();
                    boolean hasToTime = taskObj.get("hasToTime").getAsBoolean();
                    LocalDateTime from = LocalDateTime.parse(fromStr);
                    LocalDateTime to = LocalDateTime.parse(toStr);
                    task = new Event(description, from, hasFromTime, to, hasToTime);
                    break;
                default:
                    System.err.println("Unknown task type: " + type);
                    return null;
            }
            
            if (isDone) {
                task.markAsDone();
            }
            
            return task;
            
        } catch (Exception e) {
            System.err.println("Error parsing task: " + e.getMessage());
            return null;
        }
    }
}