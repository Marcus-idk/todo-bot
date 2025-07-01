package org.todobot.commands;

import org.todobot.service.TaskList;

public class FindCommand extends Command {
    public FindCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        if (arguments.length == 0) {
            return " Please provide a keyword to search for.";
        }
        return taskList.findTasks(arguments[0]);
    }
}