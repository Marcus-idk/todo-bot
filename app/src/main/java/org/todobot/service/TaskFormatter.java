package org.todobot.service;

import java.util.ArrayList;
import org.todobot.model.Task;

public class TaskFormatter {
    
    public static String formatTaskList(ArrayList<Task> tasks) {
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
    
    public static String formatSearchResults(ArrayList<Task> allTasks, ArrayList<Task> matchingTasks, String keyword) {
        if (matchingTasks.isEmpty()) {
            return " No matching tasks found for keyword: " + keyword;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(" Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            int originalIndex = allTasks.indexOf(matchingTasks.get(i)) + 1;
            sb.append("\n ").append(originalIndex).append(".").append(matchingTasks.get(i));
        }
        return sb.toString();
    }
}