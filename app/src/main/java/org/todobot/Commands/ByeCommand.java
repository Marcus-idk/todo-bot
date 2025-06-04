package org.todobot.commands;

import org.todobot.BotMessages;
import org.todobot.TaskList;

public class ByeCommand extends Command {
    public ByeCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        return BotMessages.FAREWELL;
    }
}