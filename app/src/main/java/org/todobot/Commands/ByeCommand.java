package org.todobot.commands;

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