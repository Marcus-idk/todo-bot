package org.todobot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.todobot.tasks.Deadline;
import org.todobot.tasks.Event;
import org.todobot.tasks.Task;
import org.todobot.tasks.Todo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class TaskStorage {
    private static final String DATA_DIR = "./data";
    private static final String FILE_NAME = "duke.txt";
    private static final Path FILE_PATH = Paths.get(DATA_DIR, FILE_NAME);
    
    private final Gson gson;
    
    public TaskStorage() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }
    
    public ArrayList<Task> loadTasks() {
        try {
            if (!Files.exists(FILE_PATH)) {
                return new ArrayList<>();
            }
            
            String json = Files.readString(FILE_PATH);
            if (json.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
            ArrayList<Task> tasks = new ArrayList<>();
            
            for (JsonElement element : jsonArray) {
                JsonObject taskObj = element.getAsJsonObject();
                Task task = parseTask(taskObj);
                if (task != null) {
                    tasks.add(task);
                }
            }
            
            return tasks;
            
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void saveTasks(ArrayList<Task> tasks) {
        try {
            createDataDirectoryIfNeeded();
            
            JsonArray jsonArray = new JsonArray();
            for (Task task : tasks) {
                JsonObject taskObj = new JsonObject();
                taskObj.addProperty("type", task.getClass().getSimpleName());
                taskObj.addProperty("description", task.getDescription());
                taskObj.addProperty("isDone", task.isDone());
                
                if (task instanceof Deadline) {
                    taskObj.addProperty("by", ((Deadline) task).getBy());
                } else if (task instanceof Event) {
                    taskObj.addProperty("from", ((Event) task).getFrom());
                    taskObj.addProperty("to", ((Event) task).getTo());
                }
                
                jsonArray.add(taskObj);
            }
            
            String json = gson.toJson(jsonArray);
            Files.writeString(FILE_PATH, json);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
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
                case "Todo":
                    task = new Todo(description);
                    break;
                case "Deadline":
                    String by = taskObj.get("by").getAsString();
                    task = new Deadline(description, by);
                    break;
                case "Event":
                    String from = taskObj.get("from").getAsString();
                    String to = taskObj.get("to").getAsString();
                    task = new Event(description, from, to);
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