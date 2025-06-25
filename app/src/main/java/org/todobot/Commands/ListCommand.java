package org.todobot.commands;

import org.todobot.service.TaskList;

public class ListCommand extends Command {
    public ListCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        return taskList.listTasks();
    }
}