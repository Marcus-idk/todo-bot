package org.todobot.commands;

import org.todobot.common.BotMessages;
import org.todobot.service.TaskList;

public class DeleteAllCommand extends Command {
    
    public DeleteAllCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        int deletedCount = taskList.deleteAllTasks();
        return BotMessages.formatDeletedAllTasks(deletedCount);
    }
}