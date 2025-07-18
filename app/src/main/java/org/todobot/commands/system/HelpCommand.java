package org.todobot.commands.system;

import org.todobot.commands.core.Command;
import org.todobot.common.BotMessages;
import org.todobot.service.TaskList;

public class HelpCommand extends Command {
    
    public HelpCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        return BotMessages.getHelpText();
    }
}