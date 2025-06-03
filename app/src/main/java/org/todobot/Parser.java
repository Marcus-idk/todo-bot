package org.todobot;

public class Parser {
    public static final String COMMAND_LIST = "list";
    public static final String COMMAND_BYE = "bye";
    public static final String COMMAND_MARK = "mark";
    public static final String COMMAND_UNMARK = "unmark";
    public static final String COMMAND_TODO = "todo";
    public static final String COMMAND_DEADLINE = "deadline";
    public static final String COMMAND_EVENT = "event";
    
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
    
    public boolean isTodoCommand() {
        return COMMAND_TODO.equals(command);
    }
    
    public boolean isDeadlineCommand() {
        return COMMAND_DEADLINE.equals(command);
    }
    
    public boolean isEventCommand() {
        return COMMAND_EVENT.equals(command);
    }
    
    public boolean isEmptyInput() {
        return input.isEmpty();
    }
    
    public boolean hasSpacing() {
        return input.contains(" ");
    }
    
    public boolean isValidCommand() {
        return isListCommand() || isByeCommand() || isMarkCommand() || isUnmarkCommand() 
               || isTodoCommand() || isDeadlineCommand() || isEventCommand();
    }
    
    public boolean isInvalidCommand() {
        return !isValidCommand() && !isEmptyInput();
    }
    
    public int getTaskNumber() {
        try {
            return Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    // Parse todo arguments
    public String getTodoDescription() {
        return arguments;
    }
    
    // Parse deadline arguments
    public String[] parseDeadline() {
        if (!arguments.contains("/by")) {
            return null;
        }
        
        String[] parts = arguments.split("/by", 2);
        if (parts.length != 2) {
            return null;
        }
        
        String description = parts[0].trim();
        String by = parts[1].trim();
        
        if (description.isEmpty() || by.isEmpty()) {
            return null;
        }
        
        return new String[]{description, by};
    }
    
    // Parse event arguments
    public String[] parseEvent() {
        if (!arguments.contains("/from") || !arguments.contains("/to")) {
            return null;
        }
        
        String[] fromSplit = arguments.split("/from", 2);
        if (fromSplit.length != 2) {
            return null;
        }
        
        String description = fromSplit[0].trim();
        String remaining = fromSplit[1].trim();
        
        String[] toSplit = remaining.split("/to", 2);
        if (toSplit.length != 2) {
            return null;
        }
        
        String from = toSplit[0].trim();
        String to = toSplit[1].trim();
        
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            return null;
        }
        
        return new String[]{description, from, to};
    }
}