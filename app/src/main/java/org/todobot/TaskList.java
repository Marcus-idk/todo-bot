package org.todobot;

public class TaskList {
    private Task[] tasks;
    private int taskCount;
    private static final int MAX_TASKS = 100;
    
    public TaskList() {
        tasks = new Task[MAX_TASKS];
        taskCount = 0;
    }
    
    public boolean addTask(Task task) {
        if (taskCount >= MAX_TASKS) {
            return false;
        }
        tasks[taskCount] = task;
        taskCount++;
        return true;
    }
    
    public String listTasks() {
        if (taskCount == 0) {
            return " Here are the tasks in your list:\n No tasks found! Your to-do list is as empty as my brain! 🤖";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            sb.append("\n ").append(i + 1).append(".").append(tasks[i]);
        }
        return sb.toString();
    }
    
    public boolean markTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > taskCount) {
            return false;
        }
        tasks[taskNumber - 1].markAsDone();
        return true;
    }
    
    public boolean unmarkTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > taskCount) {
            return false;
        }
        tasks[taskNumber - 1].markAsNotDone();
        return true;
    }
    
    public Task getTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > taskCount) {
            return null;
        }
        return tasks[taskNumber - 1];
    }
    
    public boolean isFull() {
        return taskCount >= MAX_TASKS;
    }
    
    public boolean isEmpty() {
        return taskCount == 0;
    }
    
    public int getTaskCount() {
        return taskCount;
    }
}