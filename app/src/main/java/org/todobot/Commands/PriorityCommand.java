package org.todobot.commands;

import org.todobot.common.BotMessages;
import org.todobot.model.Priority;
import org.todobot.model.Task;
import org.todobot.service.TaskList;

public class PriorityCommand extends Command {
    
    public PriorityCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        int taskNumber = Integer.parseInt(arguments[0]);
        String priorityToken = arguments[1];
        
        Task task = taskList.getTask(taskNumber);
        if (task == null) {
            return BotMessages.INVALID_TASK_NUMBER;
        }
        
        Priority priority = parsePriority(priorityToken);
        task.setPriority(priority);
        
        return BotMessages.formatPriorityChanged(task);
    }
    
    private Priority parsePriority(String token) {
        String normalizedToken = token.toLowerCase();
        switch (normalizedToken) {
            case "high":
            case "h":
                return Priority.HIGH;
            case "medium":
            case "m":
                return Priority.MEDIUM;
            case "low":
            case "l":
                return Priority.LOW;
            default:
                return Priority.MEDIUM; // Should not happen due to parser validation
        }
    }
}