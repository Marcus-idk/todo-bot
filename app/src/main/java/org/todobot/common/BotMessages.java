package org.todobot.common;
import org.todobot.model.Task;

public class BotMessages {
    // ANSI color codes
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    
    // Display constants
    public static final String LOGO = " _  ___   _ _   _    ____   ___ _____ \n"
            + "| |/ / | | | \\ | |  | __ ) / _ \\_   _|\n"
            + "| ' /| | | |  \\| |  |  _ \\| | | || |  \n"
            + "| . \\| |_| | |\\  |  | |_) | |_| || |  \n"
            + "|_|\\_\\\\___/|_| \\_|  |____/ \\___/ |_|  \n";
    
    public static final String HORIZONTAL_LINE = "____________________________________________________________";
    
    // Error messages
    public static final String EMPTY_INPUT = RED + " Oops! We need an input! I can't read your mind... yet! 🤔" + RESET;
    public static final String INVALID_COMMAND = RED + " Hey there! I don't recognize that command! " +
            "Type 'help' if you need to see available commands! 😅🤖" + RESET;
    public static final String TASK_LIMIT_REACHED = RED + " Whoa there! You've hit the 100-task limit! " +
            "I'm not a miracle worker! Please delete some tasks first! 😅" + RESET;
    public static final String INVALID_TASK_NUMBER = RED + " Hmm, that task number doesn't exist! " +
            "Did you count with your fingers? Try again! 🤔👆" + RESET;
    public static final String INVALID_NUMBER_FORMAT = RED + " That's not a valid number! " +
            "I may be a bot, but I still know what numbers look like! 🤖🔢" + RESET;
    
    // Format error messages
    public static final String INVALID_TODO_FORMAT = RED + " Oops! ToDo format should be: todo [description] " +
            "Come on, give me something to do! 😄" + RESET;
    public static final String INVALID_DEADLINE_FORMAT = RED + " Hey! Deadline format should be: deadline [description] /by [date] " +
            "Don't forget the /by part! I need to know when! ⏰🤖" + RESET;
    public static final String INVALID_EVENT_FORMAT = RED + " Whoops! Event format should be: event [description] /from [start] /to [end] " +
            "I need both /from and /to! Time is important! 📅😅" + RESET;
    public static final String INVALID_DATE_FORMAT = RED + " Oops! Date format should be DD-MM-YYYY or DD-MM-YYYY HH:MM " +
            "Let's keep time organized! ⏰🤖" + RESET;
    
    // Success messages
    public static String formatAddedTask(Task task, int totalTasks) {
        return " Got it. I've added this task:\n   " + task + 
               "\n Now you have " + totalTasks + " tasks in the list.";
    }
    
    public static String formatMarkedTask(Task task) {
        return " Nice! I've marked this task as done:\n   " + task;
    }
    
    public static String formatUnmarkedTask(Task task) {
        return " OK, I've marked this task as not done yet:\n   " + task;
    }
    
    public static String formatDeletedTask(Task task, int remainingTasks) {
        return " Noted. I've removed this task:\n   " + task + 
               "\n Now you have " + remainingTasks + " tasks in the list.";
    }
    
    // System messages
    public static final String FAREWELL = " Bye. Hope to see you again soon!";
    public static final String GREETING = " What can I do for you?";
}