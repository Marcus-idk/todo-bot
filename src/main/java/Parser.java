public class Parser {
    public static final String COMMAND_LIST = "list";
    public static final String COMMAND_BYE = "bye";
    public static final String COMMAND_MARK = "mark";
    public static final String COMMAND_UNMARK = "unmark";
    
    private String input;
    private String command;
    private String arguments;
    
    public Parser(String input) {
        this.input = input.trim();
        parseInput();
    }
    
    private void parseInput() {
        if (input.isEmpty()) {
            command = "";
            arguments = "";
            return;
        }
        
        String[] parts = input.split("\\s+", 2);
        command = parts[0].toLowerCase();
        arguments = parts.length > 1 ? parts[1] : "";
    }
    
    public String getCommand() {
        return command;
    }
    
    public String getArguments() {
        return arguments;
    }
    
    public String getOriginalInput() {
        return input;
    }
    
    public boolean isListCommand() {
        return COMMAND_LIST.equals(command);
    }
    
    public boolean isByeCommand() {
        return COMMAND_BYE.equals(command);
    }
    
    public boolean isMarkCommand() {
        return COMMAND_MARK.equals(command);
    }
    
    public boolean isUnmarkCommand() {
        return COMMAND_UNMARK.equals(command);
    }
    
    public boolean isEmptyInput() {
        return input.isEmpty();
    }
    
    public boolean hasSpacing() {
        return input.contains(" ");
    }
    
    public boolean isAddTaskCommand() {
        return !isListCommand() && !isByeCommand() && !isMarkCommand() && !isUnmarkCommand() 
               && !isEmptyInput() && hasSpacing();
    }
    
    public boolean isInvalidTaskFormat() {
        return !isListCommand() && !isByeCommand() && !isMarkCommand() && !isUnmarkCommand() 
               && !isEmptyInput() && !hasSpacing();
    }
    
    public int getTaskNumber() {
        try {
            return Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}