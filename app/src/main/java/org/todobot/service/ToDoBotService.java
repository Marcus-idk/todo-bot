package org.todobot.service;

import org.todobot.commands.core.Command;
import org.todobot.commands.system.HelpCommand;
import org.todobot.commands.task.AddCommand;
import org.todobot.commands.task.DeleteAllCommand;
import org.todobot.commands.task.DeleteAllConfirmCommand;
import org.todobot.commands.task.DeleteCommand;
import org.todobot.commands.task.FindCommand;
import org.todobot.commands.task.ListCommand;
import org.todobot.commands.task.MarkCommand;
import org.todobot.commands.task.PriorityCommand;
import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.parsers.core.ParseResult;
import org.todobot.parsers.core.Parser;
import org.todobot.storage.TaskStorage;

public class ToDoBotService {
    private final TaskList taskList;
    private final TaskStorage storage;
    
    public ToDoBotService() {
        this.storage = new TaskStorage();
        this.taskList = new TaskList();
        
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
            return BotMessages.FAREWELL;
        }
        
        Command command = createCommand(result.getCommandType());
        if (command == null) {
            return BotMessages.UNKNOWN_COMMAND;
        }
        
        String output = command.execute(result);
        
        // Save tasks after any command that might modify them
        if (isModifyingCommand(result.getCommandType())) {
            storage.saveTasks(taskList.getAllTasks());
        }
        
        return output;
    }
    
    private Command createCommand(CommandType commandType) {
        return switch (commandType) {
            case TODO, DEADLINE, EVENT -> new AddCommand(taskList, commandType);
            case LIST -> new ListCommand(taskList);
            case MARK -> new MarkCommand(taskList, true);
            case UNMARK -> new MarkCommand(taskList, false);
            case DELETE -> new DeleteCommand(taskList);
            case DELETE_ALL -> new DeleteAllCommand(taskList);
            case DELETE_ALL_CONFIRM -> new DeleteAllConfirmCommand(taskList);
            case FIND -> new FindCommand(taskList);
            case HELP -> new HelpCommand(taskList);
            case PRIORITY -> new PriorityCommand(taskList);
            default -> null;
        };
    }
    
    private boolean isModifyingCommand(CommandType commandType) {
        return commandType == CommandType.TODO ||
               commandType == CommandType.DEADLINE ||
               commandType == CommandType.EVENT ||
               commandType == CommandType.MARK ||
               commandType == CommandType.UNMARK ||
               commandType == CommandType.DELETE ||
               commandType == CommandType.DELETE_ALL ||
               commandType == CommandType.DELETE_ALL_CONFIRM ||
               commandType == CommandType.PRIORITY;
    }
    
    public boolean shouldExit(String input) {
        ParseResult result = Parser.parse(input);
        return result.isValid() && result.getCommandType() == CommandType.BYE;
    }
    
    public void cleanup() {
        storage.saveTasks(taskList.getAllTasks());
    }
}