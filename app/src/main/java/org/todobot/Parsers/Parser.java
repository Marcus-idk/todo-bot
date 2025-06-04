package org.todobot.parsers;
import org.todobot.BotMessages;

public class Parser {
    public static ParseResult parse(String input) {
        if (input == null) {
            return new ParseResult(BotMessages.EMPTY_INPUT);
        }
        
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return new ParseResult(BotMessages.EMPTY_INPUT);
        }
        
        String[] parts = trimmed.split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";
        
        switch (command) {
            case "list":
                return new ParseResult("list", new String[0]);
                
            case "bye":
                return new ParseResult("bye", new String[0]);
                
            case "todo":
                return parseTodo(arguments);
                
            case "deadline":
                return parseDeadline(arguments);
                
            case "event":
                return parseEvent(arguments);
                
            case "mark":
                return parseMarkUnmark("mark", arguments);
                
            case "unmark":
                return parseMarkUnmark("unmark", arguments);
                
            default:
                return new ParseResult(BotMessages.INVALID_COMMAND);
        }
    }
    
    private static ParseResult parseTodo(String arguments) {
        if (arguments.trim().isEmpty()) {
            return new ParseResult(BotMessages.INVALID_TODO_FORMAT);
        }
        return new ParseResult("todo", new String[]{arguments});
    }
    
    private static ParseResult parseDeadline(String arguments) {
        if (!arguments.contains("/by")) {
            return new ParseResult(BotMessages.INVALID_DEADLINE_FORMAT);
        }
        
        String[] parts = arguments.split("/by", 2);
        if (parts.length != 2) {
            return new ParseResult(BotMessages.INVALID_DEADLINE_FORMAT);
        }
        
        String description = parts[0].trim();
        String by = parts[1].trim();
        
        if (description.isEmpty() || by.isEmpty()) {
            return new ParseResult(BotMessages.INVALID_DEADLINE_FORMAT);
        }
        
        return new ParseResult("deadline", new String[]{description, by});
    }
    
    private static ParseResult parseEvent(String arguments) {
        if (!arguments.contains("/from") || !arguments.contains("/to")) {
            return new ParseResult(BotMessages.INVALID_EVENT_FORMAT);
        }
        
        String[] fromSplit = arguments.split("/from", 2);
        if (fromSplit.length != 2) {
            return new ParseResult(BotMessages.INVALID_EVENT_FORMAT);
        }
        
        String description = fromSplit[0].trim();
        String remaining = fromSplit[1].trim();
        
        String[] toSplit = remaining.split("/to", 2);
        if (toSplit.length != 2) {
            return new ParseResult(BotMessages.INVALID_EVENT_FORMAT);
        }
        
        String from = toSplit[0].trim();
        String to = toSplit[1].trim();
        
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            return new ParseResult(BotMessages.INVALID_EVENT_FORMAT);
        }
        
        return new ParseResult("event", new String[]{description, from, to});
    }
    
    private static ParseResult parseMarkUnmark(String command, String arguments) {
        try {
            int taskNumber = Integer.parseInt(arguments.trim());
            return new ParseResult(command, new String[]{String.valueOf(taskNumber)});
        } catch (NumberFormatException e) {
            return new ParseResult(BotMessages.INVALID_NUMBER_FORMAT);
        }
    }
}