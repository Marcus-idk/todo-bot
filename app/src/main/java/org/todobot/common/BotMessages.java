package org.todobot.common;
import org.todobot.model.Task;

public class BotMessages {
    // ANSI color codes
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    
    // Display constants
    public static final String LOGO = """
                                       _  ___   _ _   _    ____   ___ _____ 
                                      | |/ / | | | \\ | |  | __ ) / _ \\_   _|
                                      | ' /| | | |  \\| |  |  _ \\| | | || |  
                                      | . \\| |_| | |\\  |  | |_) | |_| || |  
                                      |_|\\_\\\\___/|_| \\_|  |____/ \\___/ |_|  
                                      """;
    
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
    public static final String INVALID_EVENT_TIME_ORDER = RED + " Hey! Event start time must be before end time! " +
            "Time doesn't go backwards! ⏰😅" + RESET;
    
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
    
    public static String formatDeletedAllTasks(int deletedCount) {
        if (deletedCount == 0) {
            return " No tasks to delete! Your list is already empty! 🤖";
        } else if (deletedCount == 1) {
            return " Noted. I've removed 1 task. Your list is now empty.";
        } else {
            return " Noted. I've removed all " + deletedCount + " tasks. Your list is now empty.";
        }
    }
    
    // System messages
    public static final String FAREWELL = " Bye. Hope to see you again soon!";
    public static final String GREETING = " What can I do for you?";
    public static final String UNKNOWN_COMMAND = " Unknown command type.";
    
    // === GUI INTERFACE MESSAGES ===
    public static final String GUI_SYSTEM_READY = " Task Management System Ready";
    public static final String GUI_AVAILABLE_COMMANDS = " Available commands: todo, list, help, deadline, event, bye";
    public static final String GUI_SAVING_GOODBYE = " Saving data... Goodbye.";
    
    // === TASK DISPLAY MESSAGES ===
    public static final String TASK_LIST_HEADER = " Here are the tasks in your list:";
    public static final String TASK_LIST_EMPTY = " No tasks found! Your to-do list is as empty as my brain! 🤖";
    public static final String SEARCH_RESULTS_HEADER = " Here are the matching tasks in your list:";
    public static final String SEARCH_KEYWORD_REQUIRED = " Please provide a keyword to search for.";
    
    // Dynamic search message
    public static String formatNoMatchingTasks(String keyword) {
        return " No matching tasks found for keyword: " + keyword;
    }
    
    // Complete help text
    public static String getHelpText() {
        StringBuilder help = new StringBuilder();
        help.append(" Available commands:\n\n");
        
        help.append(" Adding tasks:\n");
        help.append("   todo [description]                     - Add a simple todo task\n");
        help.append("   deadline [description] /by [date]      - Add task with deadline\n");
        help.append("   event [description] /from [start] /to [end] - Add event with time range\n\n");
        
        help.append(" Managing tasks:\n");
        help.append("   list, ls, show, display                - Show all tasks\n");
        help.append("   find, search [keyword]                 - Find tasks by keyword\n");
        help.append("   mark [number]                          - Mark task as done\n");
        help.append("   unmark [number]                        - Mark task as not done\n");
        help.append("   delete, del, d, remove, rm [number]    - Delete a task\n");
        help.append("   delete, del, d, remove, rm all         - Delete all tasks\n\n");
        
        help.append(" Other:\n");
        help.append("   help, h, ?, commands                   - Show this help message\n");
        help.append("   bye, exit, quit, goodbye               - Exit the program\n\n");
        
        help.append(" Examples:\n");
        help.append("   todo read a book\n");
        help.append("   deadline submit report /by 25-12-2024\n");
        help.append("   deadline submit report /by 25-12-2024 17:00\n");
        help.append("   event team meeting /from 25-12-2024 14:00 /to 25-12-2024 16:00\n");
        help.append("   find book\n");
        help.append("   mark 1\n");
        help.append("   delete 3\n");
        help.append("   delete all\n");
        help.append("   list");
        
        return help.toString();
    }
}