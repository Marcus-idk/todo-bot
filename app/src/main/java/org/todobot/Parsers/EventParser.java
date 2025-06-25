package org.todobot.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;

public class EventParser extends CommandParser {
    private static final Pattern EVENT_PATTERN = Pattern.compile("^(.+?)\\s*/from\\s+(.+?)\\s*/to\\s+(.+)$");
    
    @Override
    public ParseResult parse(String arguments) {
        Matcher matcher = EVENT_PATTERN.matcher(arguments);
        
        if (!matcher.matches()) {
            return new ParseResult(BotMessages.INVALID_EVENT_FORMAT);
        }
        
        String description = matcher.group(1).trim();
        String from = matcher.group(2).trim();
        String to = matcher.group(3).trim();
        
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            return new ParseResult(BotMessages.INVALID_EVENT_FORMAT);
        }
        
        return new ParseResult(CommandType.EVENT, new String[]{description, from, to});
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"event"};
    }
}