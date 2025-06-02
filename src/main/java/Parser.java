public class Parser {
    public static final String COMMAND_LIST = "list";
    public static final String COMMAND_BYE = "bye";
    
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
    
    public boolean isEmptyInput() {
        return input.isEmpty();
    }
    
    public boolean isValidTaskFormat() {
        // Check if input has at least 2 words (action + task)
        String[] words = input.split("\\s+");
        return words.length >= 2;
    }
    
    public boolean isAddTaskCommand() {
        return !isListCommand() && !isByeCommand() && !isEmptyInput() && isValidTaskFormat();
    }
    
    public boolean isInvalidTaskFormat() {
        return !isListCommand() && !isByeCommand() && !isEmptyInput() && !isValidTaskFormat();
    }
}