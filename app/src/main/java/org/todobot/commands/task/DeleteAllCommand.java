package org.todobot.commands.task;

import org.todobot.commands.core.Command;
import org.todobot.common.BotMessages;
import org.todobot.service.TaskList;

public class DeleteAllCommand extends Command {
    
    public DeleteAllCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        int taskCount = taskList.getTaskCount();
        return BotMessages.formatDeleteAllWarning(taskCount);
    }
}