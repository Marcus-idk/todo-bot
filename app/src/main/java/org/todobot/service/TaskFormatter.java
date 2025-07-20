package org.todobot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.todobot.common.BotMessages;
import org.todobot.model.Task;

public class TaskFormatter {
    
    public static String formatTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return BotMessages.TASK_LIST_HEADER + "\n" + BotMessages.TASK_LIST_EMPTY;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(BotMessages.TASK_LIST_HEADER);
        for (int i = 0; i < tasks.size(); i++) {
            sb.append("\n ").append(i + 1).append(".").append(tasks.get(i));
        }
        return sb.toString();
    }
    
    public static String formatSearchResults(ArrayList<Task> allTasks, ArrayList<Task> matchingTasks, String keyword) {
        if (matchingTasks.isEmpty()) {
            return BotMessages.formatNoMatchingTasks(keyword);
        }
        
        // Pre-compute task index mapping for O(1) lookup - fixes O(n²) performance issue
        Map<Task, Integer> taskIndexMap = new HashMap<>();
        for (int i = 0; i < allTasks.size(); i++) {
            taskIndexMap.put(allTasks.get(i), i + 1);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(BotMessages.SEARCH_RESULTS_HEADER);
        for (int i = 0; i < matchingTasks.size(); i++) {
            Integer originalIndex = taskIndexMap.get(matchingTasks.get(i));
            // Defensive check - should never be null in normal usage
            int index = originalIndex != null ? originalIndex : i + 1;
            sb.append("\n ").append(index).append(".").append(matchingTasks.get(i));
        }
        return sb.toString();
    }
}