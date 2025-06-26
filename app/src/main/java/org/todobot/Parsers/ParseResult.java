package org.todobot.parsers;

import org.todobot.common.CommandType;

public class ParseResult {
    private CommandType commandType;
    private String[] arguments;
    private Object[] timeData;
    private boolean isValid;
    private String errorMessage;
    
    public ParseResult(CommandType commandType, String[] arguments, Object[] timeData, boolean isValid, String errorMessage) {
        this.commandType = commandType;
        this.arguments = arguments;
        this.timeData = timeData;
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }
    
    // Success constructor with string arguments only
    public ParseResult(CommandType commandType, String[] arguments) {
        this(commandType, arguments, null, true, "");
    }
    
    // Success constructor with time data
    public ParseResult(CommandType commandType, String[] arguments, Object[] timeData) {
        this(commandType, arguments, timeData, true, "");
    }
    
    // Error constructor
    public ParseResult(String errorMessage) {
        this(null, new String[0], null, false, errorMessage);
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
    
    public Object[] getTimeData() {
        return timeData;
    }
    
    public boolean isEmpty() {
        return commandType == null && arguments.length == 0;
    }
}