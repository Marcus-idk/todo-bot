package org.todobot.parsers.command.task;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;
import org.todobot.parsers.util.DateTimeParser;

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
        
        // Validate and parse both dates
        try {
            DateTimeParser.DateTimeResult fromResult = DateTimeParser.parseDateTime(from);
            DateTimeParser.DateTimeResult toResult = DateTimeParser.parseDateTime(to);
            
            Object[] timeData = {
                fromResult.getDateTime(), fromResult.hasTime(),
                toResult.getDateTime(), toResult.hasTime()
            };
            
            return new ParseResult(CommandType.EVENT, new String[]{description}, timeData);
        } catch (DateTimeParseException e) {
            return new ParseResult(BotMessages.INVALID_DATE_FORMAT);
        }
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"event"};
    }
}