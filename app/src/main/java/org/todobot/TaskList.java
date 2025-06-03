package org.todobot;
public class TaskList {
    private Task[] tasks;
    private int taskCount;
    private static final int MAX_TASKS = 100;
    
    public TaskList() {
        tasks = new Task[MAX_TASKS];
        taskCount = 0;
    }
    
    public boolean addTask(String description) {
        if (taskCount >= MAX_TASKS) {
            return false;
        }
        tasks[taskCount] = new Task(description);
        taskCount++;
        return true;
    }
    
    public String listTasks() {
        if (taskCount == 0) {
            return "No tasks found! Your to-do list is as empty as my brain! 🤖";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            sb.append(" ").append(i + 1).append(". ").append(tasks[i]);
            if (i < taskCount - 1) {
                sb.append("\n");
            }
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