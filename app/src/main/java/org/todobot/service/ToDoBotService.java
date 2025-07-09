package org.todobot.service;

import org.todobot.commands.AddCommand;
import org.todobot.commands.Command;
import org.todobot.commands.DeleteCommand;
import org.todobot.commands.FindCommand;
import org.todobot.commands.HelpCommand;
import org.todobot.commands.ListCommand;
import org.todobot.commands.MarkCommand;
import org.todobot.common.CommandType;
import org.todobot.parsers.ParseResult;
import org.todobot.parsers.Parser;
import org.todobot.storage.TaskStorage;

public class ToDoBotService {
    private final TaskList taskList;
    private final TaskStorage storage;
    private boolean textInputMode = false; // Default is button mode
    
    public ToDoBotService() {
        this.storage = new TaskStorage();
        this.taskList = new TaskList();
        
        // Load tasks on startup
        taskList.setTasks(storage.loadTasks());
    }
    
    public String processCommand(String input) {
        ParseResult result = Parser.parse(input);
        
        if (!result.isValid()) {
            return getResponseForMode(result.getErrorMessage());
        }
        
        // Handle bye command
        if (result.getCommandType() == CommandType.BYE) {
            return " Bye. Hope to see you again soon!";
        }
        
        Command command = createCommand(result.getCommandType());
        if (command == null) {
            return getResponseForMode(" Unknown command type.");
        }
        
        String output = command.execute(result);
        
        // Save tasks after any command that might modify them
        if (isModifyingCommand(result.getCommandType())) {
            storage.saveTasks(taskList.getAllTasks());
        }
        
        return getResponseForMode(output);
    }
    
    private String getResponseForMode(String baseResponse) {
        if (textInputMode) {
            return baseResponse; // Input-oriented response
        } else {
            return getButtonOrientedResponse(baseResponse); // Button-oriented response
        }
    }
    
    private String getButtonOrientedResponse(String baseResponse) {
        // For now, return the same response but we'll enhance this later
        // This is where we'll add button logic
        return baseResponse;
    }
    
    public void setTextInputMode(boolean textInputMode) {
        this.textInputMode = textInputMode;
    }
    
    public boolean isTextInputMode() {
        return textInputMode;
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
    
    public boolean shouldExit(String input) {
        ParseResult result = Parser.parse(input);
        return result.isValid() && result.getCommandType() == CommandType.BYE;
    }
    
    public void cleanup() {
        // Final save before exit
        storage.saveTasks(taskList.getAllTasks());
    }
}