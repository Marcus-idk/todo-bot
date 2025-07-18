package org.todobot.commands.system;

import org.todobot.commands.core.Command;
import org.todobot.common.BotMessages;
import org.todobot.service.TaskList;

public class ByeCommand extends Command {
    public ByeCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        return BotMessages.FAREWELL;
    }
}