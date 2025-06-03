package org.todobot;
import java.util.Scanner;

public class ToDoBotCLI {
    private static final String LOGO = " _  ___   _ _   _    ____   ___ _____ \n"
            + "| |/ / | | | \\ | |  | __ ) / _ \\_   _|\n"
            + "| ' /| | | |  \\| |  |  _ \\| | | || |  \n"
            + "| . \\| |_| | |\\  |  | |_) | |_| || |  \n"
            + "|_|\\_\\\\___/|_| \\_|  |____/ \\___/ |_|  \n";
    
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    
    private TaskList taskList;
    
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
            Parser parser = new Parser(input);
            
            if (parser.isByeCommand()) {
                break;
            }
            
            if (parser.isEmptyInput()) {
                System.out.println(BotMessages.EMPTY_INPUT);
            } else if (parser.isInvalidTaskFormat()) {
                System.out.println(BotMessages.INVALID_FORMAT);
            } else if (parser.isListCommand()) {
                System.out.println(taskList.listTasks());
            } else if (parser.isMarkCommand()) {
                handleMarkCommand(parser);
            } else if (parser.isUnmarkCommand()) {
                handleUnmarkCommand(parser);
            } else if (parser.isAddTaskCommand()) {
                if (taskList.isFull()) {
                    System.out.println(BotMessages.TASK_LIMIT_REACHED);
                } else {
                    taskList.addTask(parser.getOriginalInput());
                    System.out.println(BotMessages.formatAddedTask(parser.getOriginalInput()));
                }
            }
            
            System.out.println(HORIZONTAL_LINE);
        }
        scanner.close();
    }
    
    private void handleMarkCommand(Parser parser) {
        int taskNumber = parser.getTaskNumber();
        if (taskNumber == -1) {
            System.out.println(BotMessages.INVALID_NUMBER_FORMAT);
        } else if (!taskList.markTask(taskNumber)) {
            System.out.println(BotMessages.INVALID_TASK_NUMBER);
        } else {
            Task task = taskList.getTask(taskNumber);
            System.out.println(BotMessages.formatMarkedTask(task));
        }
    }
    
    private void handleUnmarkCommand(Parser parser) {
        int taskNumber = parser.getTaskNumber();
        if (taskNumber == -1) {
            System.out.println(BotMessages.INVALID_NUMBER_FORMAT);
        } else if (!taskList.unmarkTask(taskNumber)) {
            System.out.println(BotMessages.INVALID_TASK_NUMBER);
        } else {
            Task task = taskList.getTask(taskNumber);
            System.out.println(BotMessages.formatUnmarkedTask(task));
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