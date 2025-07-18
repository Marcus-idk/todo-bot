package org.todobot.commands.task;

import org.todobot.commands.core.Command;
import org.todobot.common.BotMessages;
import org.todobot.service.TaskList;

public class FindCommand extends Command {
    public FindCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        if (arguments.length == 0) {
            return BotMessages.SEARCH_KEYWORD_REQUIRED;
        }
        return taskList.findTasks(arguments[0]);
    }
}