package org.todobot.commands;
import org.todobot.parsers.ParseResult;
import org.todobot.service.TaskList;

public abstract class Command {
    protected TaskList taskList;
    
    public Command(TaskList taskList) {
        this.taskList = taskList;
    }
    
    public abstract String execute(String[] arguments);
    
    public String execute(ParseResult parseResult) {
        return execute(parseResult.getArguments());
    }
}