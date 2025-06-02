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
    
    // Success messages
    public static String formatAddedTask(String task) {
        return " added: " + task;
    }
    
    // System messages
    public static final String FAREWELL = " Bye. Hope to see you again soon!";
    public static final String GREETING = " What can I do for you?";
}