package org.todobot.commands;
import java.time.LocalDateTime;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.model.Deadline;
import org.todobot.model.Event;
import org.todobot.model.Task;
import org.todobot.model.Todo;
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
        String description = arguments[0];
        
        Task task;
        switch (taskType) {
            case TODO -> task = new Todo(description);
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
                task = new Event(description, fromDateTime, hasFromTime, toDateTime, hasToTime);
            }
            default -> {
                return BotMessages.INVALID_COMMAND;
            }
        }
        
        taskList.addTask(task);
        return BotMessages.formatAddedTask(task, taskList.getTaskCount());
    }
}