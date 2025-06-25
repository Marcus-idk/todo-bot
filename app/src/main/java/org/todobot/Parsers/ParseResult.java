package org.todobot.parsers;

import org.todobot.common.CommandType;

public class ParseResult {
    private CommandType commandType;
    private String[] arguments;
    private boolean isValid;
    private String errorMessage;
    
    public ParseResult(CommandType commandType, String[] arguments, boolean isValid, String errorMessage) {
        this.commandType = commandType;
        this.arguments = arguments;
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }
    
    // Success constructor
    public ParseResult(CommandType commandType, String[] arguments) {
        this(commandType, arguments, true, "");
    }
    
    // Error constructor
    public ParseResult(String errorMessage) {
        this(null, new String[0], false, errorMessage);
    }
    
    public CommandType getCommandType() {
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
        return commandType == null && arguments.length == 0;
    }
}