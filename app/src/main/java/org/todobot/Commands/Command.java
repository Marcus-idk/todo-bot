package org.todobot.commands;
import org.todobot.TaskList;

public abstract class Command {
    protected TaskList taskList;
    
    public Command(TaskList taskList) {
        this.taskList = taskList;
    }
    
    public abstract String execute(String[] arguments);
}