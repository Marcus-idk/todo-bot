package org.todobot;

import org.todobot.commands.AddCommand;
import org.todobot.commands.Command;
import org.todobot.commands.ListCommand;
import org.todobot.commands.MarkCommand;
import org.todobot.parsers.ParseResult;
import org.todobot.parsers.Parser;
import org.todobot.ui.UI;

public class ToDoBotCLI {
    private final TaskList taskList;
    private final UI ui;
    
    public ToDoBotCLI() {
        taskList = new TaskList();
        ui = new UI();
    }
    
    private void handleUserInput() {
        String input;
        
        while (true) {
            input = ui.readCommand();
            ParseResult result = Parser.parse(input);
            
            if (!result.isValid()) {
                ui.showResponse(result.getErrorMessage());
            } else if (result.getCommandType() == CommandType.BYE) {
                break;
            } else {
                Command command = createCommand(result.getCommandType());
                String output = command.execute(result.getArguments());
                ui.showResponse(output);
            }
            
            ui.showLine();
        }
    }
    
    private Command createCommand(CommandType commandType) {
        switch (commandType) {
            case TODO:
            case DEADLINE:
            case EVENT:
                return new AddCommand(taskList, commandType);
            case LIST:
                return new ListCommand(taskList);
            case MARK:
                return new MarkCommand(taskList, true);
            case UNMARK:
                return new MarkCommand(taskList, false);
            default:
                // This shouldn't happen since Parser validates commands
                return null;
        }
    }
    
    public static void main(String[] args) {
        ToDoBotCLI bot = new ToDoBotCLI();
        bot.ui.showGreeting();
        bot.handleUserInput();
        bot.ui.showFarewell();
        bot.ui.close();
    }
}