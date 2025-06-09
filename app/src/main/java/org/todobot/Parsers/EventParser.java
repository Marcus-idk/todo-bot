package org.todobot.parsers;

import org.todobot.BotMessages;

public class EventParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
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
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"event"};
    }
}