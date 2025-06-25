package org.todobot;

import java.util.ArrayList;

import org.todobot.tasks.Task;

public class TaskList {
    private final ArrayList<Task> tasks;
    private static final int MAX_TASKS = 100;
    
    public TaskList() {
        tasks = new ArrayList<>();
    }
    
    public boolean addTask(Task task) {
        if (tasks.size() >= MAX_TASKS) {
            return false;
        }
        tasks.add(task);
        return true;
    }
    
    public String listTasks() {
        if (tasks.isEmpty()) {
            return " Here are the tasks in your list:\n No tasks found! Your to-do list is as empty as my brain! 🤖";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append("\n ").append(i + 1).append(".").append(tasks.get(i));
        }
        return sb.toString();
    }
    
    public boolean markTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            return false;
        }
        tasks.get(taskNumber - 1).markAsDone();
        return true;
    }
    
    public boolean unmarkTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            return false;
        }
        tasks.get(taskNumber - 1).markAsNotDone();
        return true;
    }
    
    public Task getTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.get(taskNumber - 1);
    }
    
    public boolean isFull() {
        return tasks.size() >= MAX_TASKS;
    }
    
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
    
    public int getTaskCount() {
        return tasks.size();
    }
    
    public Task deleteTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.remove(taskNumber - 1);
    }
}