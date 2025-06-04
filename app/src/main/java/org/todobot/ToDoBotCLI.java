package org.todobot;
import java.util.Scanner;

import org.todobot.commands.AddCommand;
import org.todobot.commands.Command;
import org.todobot.commands.ListCommand;
import org.todobot.commands.MarkCommand;
import org.todobot.parsers.ParseResult;
import org.todobot.parsers.Parser;

public class ToDoBotCLI {
    private static final String LOGO = " _  ___   _ _   _    ____   ___ _____ \n"
            + "| |/ / | | | \\ | |  | __ ) / _ \\_   _|\n"
            + "| ' /| | | |  \\| |  |  _ \\| | | || |  \n"
            + "| . \\| |_| | |\\  |  | |_) | |_| || |  \n"
            + "|_|\\_\\\\___/|_| \\_|  |____/ \\___/ |_|  \n";
    
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    
    private final TaskList taskList;
    
    public ToDoBotCLI() {
        taskList = new TaskList();
    }
    
    private void printGreeting() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(LOGO);
        System.out.println(BotMessages.GREETING);
        System.out.println(HORIZONTAL_LINE);
    }
    
    private void handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        String input;
        
        while (true) {
            input = scanner.nextLine();
            ParseResult result = Parser.parse(input);
            
            if (!result.isValid()) {
                System.out.println(result.getErrorMessage());
            } else if (result.getCommandType().equals("bye")) {
                break;
            } else {
                Command command = createCommand(result.getCommandType());
                String output = command.execute(result.getArguments());
                System.out.println(output);
            }
            
            System.out.println(HORIZONTAL_LINE);
        }
        scanner.close();
    }
    
    private Command createCommand(String commandType) {
        switch (commandType) {
            case "todo":
            case "deadline":
            case "event":
                return new AddCommand(taskList, commandType);
            case "list":
                return new ListCommand(taskList);
            case "mark":
                return new MarkCommand(taskList, true);
            case "unmark":
                return new MarkCommand(taskList, false);
            default:
                // This shouldn't happen since Parser validates commands
                return null;
        }
    }
    
    private void printFarewell() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BotMessages.FAREWELL);
        System.out.println(HORIZONTAL_LINE);
    }
    
    public static void main(String[] args) {
        ToDoBotCLI bot = new ToDoBotCLI();
        bot.printGreeting();
        bot.handleUserInput();
        bot.printFarewell();
    }
}