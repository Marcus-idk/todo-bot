package org.todobot.parsers;

public class ParseResult {
    private String commandType;
    private String[] arguments;
    private boolean isValid;
    private String errorMessage;
    
    public ParseResult(String commandType, String[] arguments, boolean isValid, String errorMessage) {
        this.commandType = commandType;
        this.arguments = arguments;
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }
    
    // Success constructor
    public ParseResult(String commandType, String[] arguments) {
        this(commandType, arguments, true, "");
    }
    
    // Error constructor
    public ParseResult(String errorMessage) {
        this("", new String[0], false, errorMessage);
    }
    
    public String getCommandType() {
        return commandType;
    }
    
    public String[] getArguments() {
        return arguments;
    }
    
    public boolean isValid() {
        return isValid;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public boolean isEmpty() {
        return commandType.isEmpty() && arguments.length == 0;
    }
}