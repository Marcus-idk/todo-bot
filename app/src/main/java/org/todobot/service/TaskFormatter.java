package org.todobot.service;

import java.util.ArrayList;
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
        
        StringBuilder sb = new StringBuilder();
        sb.append(BotMessages.SEARCH_RESULTS_HEADER);
        for (int i = 0; i < matchingTasks.size(); i++) {
            int originalIndex = allTasks.indexOf(matchingTasks.get(i)) + 1;
            sb.append("\n ").append(originalIndex).append(".").append(matchingTasks.get(i));
        }
        return sb.toString();
    }
}