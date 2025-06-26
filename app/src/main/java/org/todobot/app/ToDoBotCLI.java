package org.todobot.app;

import org.todobot.commands.AddCommand;
import org.todobot.commands.Command;
import org.todobot.commands.DeleteCommand;
import org.todobot.commands.HelpCommand;
import org.todobot.commands.ListCommand;
import org.todobot.commands.MarkCommand;
import org.todobot.common.CommandType;
import org.todobot.parsers.ParseResult;
import org.todobot.parsers.Parser;
import org.todobot.service.TaskList;
import org.todobot.storage.TaskStorage;
import org.todobot.ui.UI;

public class ToDoBotCLI {
    private final TaskList taskList;
    private final TaskStorage storage;
    private final UI ui;
    
    public ToDoBotCLI() {
        this.storage = new TaskStorage();
        this.taskList = new TaskList();
        this.ui = new UI();
        
        // Load tasks on startup
        taskList.setTasks(storage.loadTasks());
    }
    
    private void handleUserInput() {
        while (true) {
            String input = ui.readCommand();
            ParseResult result = Parser.parse(input);
            
            if (shouldExit(result)) {
                break;
            }
            
            processCommand(result);
            ui.showLine();
        }
    }
    
    private boolean shouldExit(ParseResult result) {
        return result.isValid() && result.getCommandType() == CommandType.BYE;
    }
    
    private void processCommand(ParseResult result) {
        if (!result.isValid()) {
            ui.showResponse(result.getErrorMessage());
            return;
        }
        
        Command command = createCommand(result.getCommandType());
        String output = command.execute(result);
        ui.showResponse(output);
        
        // Save tasks after any command that might modify them
        if (isModifyingCommand(result.getCommandType())) {
            storage.saveTasks(taskList.getAllTasks());
        }
    }
    
    private Command createCommand(CommandType commandType) {
        return switch (commandType) {
            case TODO, DEADLINE, EVENT -> new AddCommand(taskList, commandType);
            case LIST -> new ListCommand(taskList);
            case MARK -> new MarkCommand(taskList, true);
            case UNMARK -> new MarkCommand(taskList, false);
            case DELETE -> new DeleteCommand(taskList);
            case HELP -> new HelpCommand(taskList);
            default -> null;
        };
    }
    
    public void run() {
        ui.showGreeting();
        handleUserInput();
        ui.showFarewell();
    }
    
    private boolean isModifyingCommand(CommandType commandType) {
        return commandType == CommandType.TODO ||
               commandType == CommandType.DEADLINE ||
               commandType == CommandType.EVENT ||
               commandType == CommandType.MARK ||
               commandType == CommandType.UNMARK ||
               commandType == CommandType.DELETE;
    }
    
    public void cleanup() {
        ui.close();
    }
}