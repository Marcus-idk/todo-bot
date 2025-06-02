public class BotMessages {
    // ANSI color codes
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    
    // Error messages
    public static final String EMPTY_INPUT = RED + " Oops! We need an input! I can't read your mind... yet! 🤔" + RESET;
    public static final String INVALID_FORMAT = RED + " Hey there! Please key in an actual action + task, separated by a space! " +
            "I'm not a mind reader, give me something to work with! 😅🤖" + RESET;
    public static final String TASK_LIMIT_REACHED = RED + " Whoa there! You've hit the 100-task limit! " +
            "I'm not a miracle worker! Please delete some tasks first! 😅" + RESET;
    public static final String INVALID_TASK_NUMBER = RED + " Hmm, that task number doesn't exist! " +
            "Did you count with your fingers? Try again! 🤔👆" + RESET;
    public static final String INVALID_NUMBER_FORMAT = RED + " That's not a valid number! " +
            "I may be a bot, but I still know what numbers look like! 🤖🔢" + RESET;
    
    // Success messages
    public static String formatAddedTask(String task) {
        return " added: " + task;
    }
    
    public static String formatMarkedTask(Task task) {
        return " Nice! I've marked this task as done:\n   " + task;
    }
    
    public static String formatUnmarkedTask(Task task) {
        return " OK, I've marked this task as not done yet:\n   " + task;
    }
    
    // System messages
    public static final String FAREWELL = " Bye. Hope to see you again soon!";
    public static final String GREETING = " What can I do for you?";
}