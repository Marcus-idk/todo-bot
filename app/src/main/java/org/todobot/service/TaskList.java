package org.todobot.service;

import java.util.ArrayList;

import org.todobot.model.Task;

public class TaskList {
    private final ArrayList<Task> tasks;
    private static final int MAX_TASKS = 100;
    
    public TaskList() {
        this.tasks = new ArrayList<>();
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
    
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }
    
    public String findTasks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return " Please provide a keyword to search for.";
        }
        
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase().trim();
        
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }
        
        if (matchingTasks.isEmpty()) {
            return " No matching tasks found for keyword: " + keyword;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(" Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            int originalIndex = tasks.indexOf(matchingTasks.get(i)) + 1;
            sb.append("\n ").append(originalIndex).append(".").append(matchingTasks.get(i));
        }
        return sb.toString();
    }
}