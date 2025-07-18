package org.todobot.commands.task;

import org.todobot.commands.core.Command;
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