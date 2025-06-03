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
            } else if (parser.isListCommand()) {
                System.out.println(taskList.listTasks());
            } else if (parser.isMarkCommand()) {
                handleMarkCommand(parser);
            } else if (parser.isUnmarkCommand()) {
                handleUnmarkCommand(parser);
            } else if (parser.isTodoCommand()) {
                handleTodoCommand(parser);
            } else if (parser.isDeadlineCommand()) {
                handleDeadlineCommand(parser);
            } else if (parser.isEventCommand()) {
                handleEventCommand(parser);
            } else {
                System.out.println(BotMessages.INVALID_COMMAND);
            }
            
            System.out.println(HORIZONTAL_LINE);
        }
        scanner.close();
    }
    
    private void handleTodoCommand(Parser parser) {
        String description = parser.getTodoDescription();
        if (description.isEmpty()) {
            System.out.println(BotMessages.INVALID_TODO_FORMAT);
            return;
        }
        
        if (taskList.isFull()) {
            System.out.println(BotMessages.TASK_LIMIT_REACHED);
            return;
        }
        
        Todo todo = new Todo(description);
        taskList.addTask(todo);
        System.out.println(BotMessages.formatAddedTask(todo, taskList.getTaskCount()));
    }
    
    private void handleDeadlineCommand(Parser parser) {
        String[] parsed = parser.parseDeadline();
        if (parsed == null) {
            System.out.println(BotMessages.INVALID_DEADLINE_FORMAT);
            return;
        }
        
        if (taskList.isFull()) {
            System.out.println(BotMessages.TASK_LIMIT_REACHED);
            return;
        }
        
        Deadline deadline = new Deadline(parsed[0], parsed[1]);
        taskList.addTask(deadline);
        System.out.println(BotMessages.formatAddedTask(deadline, taskList.getTaskCount()));
    }
    
    private void handleEventCommand(Parser parser) {
        String[] parsed = parser.parseEvent();
        if (parsed == null) {
            System.out.println(BotMessages.INVALID_EVENT_FORMAT);
            return;
        }
        
        if (taskList.isFull()) {
            System.out.println(BotMessages.TASK_LIMIT_REACHED);
            return;
        }
        
        Event event = new Event(parsed[0], parsed[1], parsed[2]);
        taskList.addTask(event);
        System.out.println(BotMessages.formatAddedTask(event, taskList.getTaskCount()));
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