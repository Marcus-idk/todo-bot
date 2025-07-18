package org.todobot.commands.task;

import org.todobot.commands.core.Command;
import org.todobot.common.BotMessages;
import org.todobot.model.Task;
import org.todobot.service.TaskList;

public class DeleteCommand extends Command {
    
    public DeleteCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        int taskNumber = Integer.parseInt(arguments[0]);
        
        Task deletedTask = taskList.deleteTask(taskNumber);
        if (deletedTask == null) {
            return BotMessages.INVALID_TASK_NUMBER;
        }
        
        return BotMessages.formatDeletedTask(deletedTask, taskList.getTaskCount());
    }
}