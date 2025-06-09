package org.todobot.parsers;

import org.todobot.BotMessages;

public class DeadlineParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
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
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"deadline"};
    }
}