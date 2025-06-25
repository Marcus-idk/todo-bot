package org.todobot.commands;

import org.todobot.BotMessages;
import org.todobot.TaskList;
import org.todobot.tasks.Task;

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