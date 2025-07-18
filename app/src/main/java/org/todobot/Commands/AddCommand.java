package org.todobot.commands;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.model.Deadline;
import org.todobot.model.Event;
import org.todobot.model.Priority;
import org.todobot.model.Task;
import org.todobot.model.ToDo;
import org.todobot.parsers.ParseResult;
import org.todobot.service.TaskList;

public class AddCommand extends Command {
    private final CommandType taskType;
    
    public AddCommand(TaskList taskList, CommandType taskType) {
        super(taskList);
        this.taskType = taskType;
    }
    
    @Override
    public String execute(String[] arguments) {
        throw new UnsupportedOperationException("AddCommand requires ParseResult with typed data");
    }
    
    @Override
    public String execute(ParseResult parseResult) {
        if (taskList.isFull()) {
            return BotMessages.TASK_LIMIT_REACHED;
        }
        
        String[] arguments = parseResult.getArguments();
        Object[] timeData = parseResult.getTimeData();
        String fullDescription = arguments[0];
        
        Priority priority = extractPriority(fullDescription);
        String description = removePriority(fullDescription);
        
        Task task;
        switch (taskType) {
            case TODO -> task = new ToDo(description);
            case DEADLINE -> {
                if (timeData == null || timeData.length < 2) {
                    return BotMessages.INVALID_DATE_FORMAT;
                }
                // timeData[0] = LocalDateTime, timeData[1] = boolean hasTime
                LocalDateTime dateTime = (LocalDateTime) timeData[0];
                boolean hasTime = (Boolean) timeData[1];
                task = new Deadline(description, dateTime, hasTime);
            }
            case EVENT -> {
                if (timeData == null || timeData.length < 4) {
                    return BotMessages.INVALID_DATE_FORMAT;
                }
                // timeData[0] = from LocalDateTime, timeData[1] = from hasTime
                // timeData[2] = to LocalDateTime, timeData[3] = to hasTime  
                LocalDateTime fromDateTime = (LocalDateTime) timeData[0];
                boolean hasFromTime = (Boolean) timeData[1];
                LocalDateTime toDateTime = (LocalDateTime) timeData[2];
                boolean hasToTime = (Boolean) timeData[3];
                try {
                    task = new Event(description, fromDateTime, hasFromTime, toDateTime, hasToTime);
                } catch (IllegalArgumentException e) {
                    return BotMessages.INVALID_EVENT_TIME_ORDER;
                }
            }
            default -> {
                return BotMessages.INVALID_COMMAND;
            }
        }
        
        task.setPriority(priority);
        taskList.addTask(task);
        return BotMessages.formatAddedTask(task, taskList.getTaskCount());
    }
    
    private Priority extractPriority(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Priority.MEDIUM;
        }
        
        Pattern pattern = Pattern.compile("!([a-zA-Z]+)");
        Matcher matcher = pattern.matcher(input);
        
        if (matcher.find()) {
            String token = matcher.group(1).toLowerCase();
            switch (token) {
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
                    return Priority.MEDIUM; // invalid priority defaults to medium
            }
        }
        
        return Priority.MEDIUM; // no priority found
    }
    
    private String removePriority(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        return input.replaceFirst("![a-zA-Z]+", "")
                   .replaceAll("\\s+", " ")
                   .trim();
    }
}