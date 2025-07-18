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
    public static final String EMPTY_INPUT = RED + " Please enter a command! I'm ready to help." + RESET;
    public static final String INVALID_COMMAND = RED + " I don't recognize that command. " +
            "Type 'help' to see what I can do for you!" + RESET;
    public static final String TASK_LIMIT_REACHED = RED + " You've reached the 100-task limit! " +
            "Please delete some tasks before adding new ones." + RESET;
    public static final String INVALID_TASK_NUMBER = RED + " That task number doesn't exist. " +
            "Please check your task list and try again." + RESET;
    public static final String INVALID_NUMBER_FORMAT = RED + " Please enter a valid number." + RESET;
    public static final String INVALID_PRIORITY_FORMAT = RED + " Priority format should be: priority [task number] [priority level] " +
            "Example: priority 3 high" + RESET;
    public static final String INVALID_PRIORITY_LEVEL = RED + " Priority level should be high/h, medium/m, or low/l" + RESET;
    
    // Format error messages
    public static final String INVALID_TODO_FORMAT = RED + " Todo format should be: todo [description]" + RESET;
    public static final String INVALID_DEADLINE_FORMAT = RED + " Deadline format should be: deadline [description] /by [date] " +
            "Don't forget the /by part!" + RESET;
    public static final String INVALID_EVENT_FORMAT = RED + " Event format should be: event [description] /from [start] /to [end] " +
            "Both /from and /to are required." + RESET;
    public static final String INVALID_DATE_FORMAT = RED + " Date format should be DD-MM-YYYY or DD-MM-YYYY HH:MM" + RESET;
    public static final String INVALID_EVENT_TIME_ORDER = RED + " Event start time must be before end time." + RESET;
    
    // Success messages
    public static String formatAddedTask(Task task, int totalTasks) {
        return " Got it! I've added this task:\n   " + task + 
               "\n Now you have " + totalTasks + " tasks in the list.";
    }
    
    public static String formatMarkedTask(Task task) {
        return " Great! I've marked this task as done:\n   " + task;
    }
    
    public static String formatUnmarkedTask(Task task) {
        return " Alright, I've marked this task as not done yet:\n   " + task;
    }
    
    public static String formatDeletedTask(Task task, int remainingTasks) {
        return " Done! I've removed this task:\n   " + task + 
               "\n Now you have " + remainingTasks + " tasks in the list.";
    }
    
    public static String formatDeletedAllTasks(int deletedCount) {
        if (deletedCount == 0) {
            return " No tasks to delete! Your list is already empty.";
        } else if (deletedCount == 1) {
            return " Done! I've removed 1 task. Your list is now empty.";
        } else {
            return " Done! I've removed all " + deletedCount + " tasks. Your list is now empty.";
        }
    }
    
    public static String formatPriorityChanged(Task task) {
        return " Perfect! I've updated the priority for this task:\n   " + task;
    }
    
    public static String formatDeleteAllWarning(int taskCount) {
        if (taskCount == 0) {
            return " No tasks to delete! Your list is already empty.";
        } else if (taskCount == 1) {
            return RED + " WARNING: This will permanently delete 1 task!" + RESET + 
                   "\n Type 'delete all confirm' to proceed, or any other command to cancel.";
        } else {
            return RED + " WARNING: This will permanently delete all " + taskCount + " tasks!" + RESET + 
                   "\n Type 'delete all confirm' to proceed, or any other command to cancel.";
        }
    }
    
    // System messages
    public static final String FAREWELL = " Goodbye! Hope to see you again soon!";
    public static final String GREETING = " Hi there! What can I do for you?";
    public static final String UNKNOWN_COMMAND = " Unknown command type.";
    
    // === GUI INTERFACE MESSAGES ===
    public static final String GUI_SYSTEM_READY = " Task Management System Ready";
    public static final String GUI_AVAILABLE_COMMANDS = " Available commands: todo, list, help, deadline, event, bye";
    public static final String GUI_SAVING_GOODBYE = " Saving data... Goodbye!";
    
    // === TASK DISPLAY MESSAGES ===
    public static final String TASK_LIST_HEADER = " Here are the tasks in your list:";
    public static final String TASK_LIST_EMPTY = " No tasks found! Your to-do list is completely empty.";
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
        help.append("   priority [number] [level]              - Change task priority (high/medium/low)\n");
        help.append("   delete, del, d, remove, rm [number]    - Delete a task\n");
        help.append("   delete, del, d, remove, rm all         - Show delete all confirmation\n");
        help.append("   delete, del, d, remove, rm all confirm - Actually delete all tasks\n\n");
        
        help.append(" Other:\n");
        help.append("   help, h, ?, commands                   - Show this help message\n");
        help.append("   bye, exit, quit, goodbye               - Exit the program\n\n");
        
        help.append(" Examples:\n");
        help.append("   todo read a book !high\n");
        help.append("   deadline submit report /by 25-12-2024\n");
        help.append("   deadline submit report /by 25-12-2024 17:00\n");
        help.append("   event team meeting /from 25-12-2024 14:00 /to 25-12-2024 16:00\n");
        help.append("   find book\n");
        help.append("   mark 1\n");
        help.append("   priority 3 high\n");
        help.append("   delete 3\n");
        help.append("   delete all\n");
        help.append("   delete all confirm\n");
        help.append("   list");
        
        return help.toString();
    }
}