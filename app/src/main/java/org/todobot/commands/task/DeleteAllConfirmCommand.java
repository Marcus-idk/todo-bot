package org.todobot.commands.task;

import org.todobot.commands.core.Command;
import org.todobot.common.BotMessages;
import org.todobot.service.TaskList;

public class DeleteAllConfirmCommand extends Command {
    
    public DeleteAllConfirmCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        int deletedCount = taskList.deleteAllTasks();
        return BotMessages.formatDeletedAllTasks(deletedCount);
    }
}