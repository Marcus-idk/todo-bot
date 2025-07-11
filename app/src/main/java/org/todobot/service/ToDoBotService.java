package org.todobot.service;

import org.todobot.commands.AddCommand;
import org.todobot.commands.Command;
import org.todobot.commands.DeleteCommand;
import org.todobot.commands.FindCommand;
import org.todobot.commands.HelpCommand;
import org.todobot.commands.ListCommand;
import org.todobot.commands.MarkCommand;
import org.todobot.common.CommandType;
import org.todobot.gui.ButtonResponseHandler;
import org.todobot.parsers.ParseResult;
import org.todobot.parsers.Parser;
import org.todobot.storage.TaskStorage;

public class ToDoBotService {
    private final TaskList taskList;
    private final TaskStorage storage;
    private final ButtonResponseHandler buttonResponseHandler;
    private boolean shouldExit = false; // Flag for exit state
    
    public ToDoBotService() {
        this.storage = new TaskStorage();
        this.taskList = new TaskList();
        this.buttonResponseHandler = new ButtonResponseHandler(this::processCommand, taskList);
        
        // Load tasks on startup
        taskList.setTasks(storage.loadTasks());
    }
    
    public String processCommand(String input) {
        ParseResult result = Parser.parse(input);
        
        if (!result.isValid()) {
            return result.getErrorMessage();
        }
        
        // Handle bye command
        if (result.getCommandType() == CommandType.BYE) {
            shouldExit = true;
            return " Bye. Hope to see you again soon!";
        }
        
        Command command = createCommand(result.getCommandType());
        if (command == null) {
            return " Unknown command type.";
        }
        
        String output = command.execute(result);
        
        // Save tasks after any command that might modify them
        if (isModifyingCommand(result.getCommandType())) {
            storage.saveTasks(taskList.getAllTasks());
        }
        
        return output;
    }
    
    public String processButtonClick(String buttonAction) {
        return buttonResponseHandler.processButtonClick(buttonAction);
    }
    
    public String handleDropdownSelection(String selectedTask, String selectedAction) {
        return buttonResponseHandler.handleDropdownSelection(selectedTask, selectedAction);
    }
    
    public String buildTaskCommand(String taskType, String taskText) {
        return buttonResponseHandler.buildTaskCommand(taskType, taskText);
    }
    
    private Command createCommand(CommandType commandType) {
        return switch (commandType) {
            case TODO, DEADLINE, EVENT -> new AddCommand(taskList, commandType);
            case LIST -> new ListCommand(taskList);
            case MARK -> new MarkCommand(taskList, true);
            case UNMARK -> new MarkCommand(taskList, false);
            case DELETE -> new DeleteCommand(taskList);
            case FIND -> new FindCommand(taskList);
            case HELP -> new HelpCommand(taskList);
            default -> null;
        };
    }
    
    private boolean isModifyingCommand(CommandType commandType) {
        return commandType == CommandType.TODO ||
               commandType == CommandType.DEADLINE ||
               commandType == CommandType.EVENT ||
               commandType == CommandType.MARK ||
               commandType == CommandType.UNMARK ||
               commandType == CommandType.DELETE;
    }
    
    public boolean shouldExit() {
        return shouldExit;
    }
    
    public void cleanup() {
        // Final save before exit
        storage.saveTasks(taskList.getAllTasks());
    }
}