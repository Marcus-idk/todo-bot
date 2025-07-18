package org.todobot.parsers.command.task;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;
import org.todobot.parsers.util.DateTimeParser;

public class DeadlineParser extends CommandParser {
    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^(.+?)\\s*/by\\s+(.+)$");
    
    @Override
    public ParseResult parse(String arguments) {
        Matcher matcher = DEADLINE_PATTERN.matcher(arguments);
        
        if (!matcher.matches()) {
            return new ParseResult(BotMessages.INVALID_DEADLINE_FORMAT);
        }
        
        String description = matcher.group(1).trim();
        String by = matcher.group(2).trim();
        
        if (description.isEmpty() || by.isEmpty()) {
            return new ParseResult(BotMessages.INVALID_DEADLINE_FORMAT);
        }
        
        // Validate and parse the date/time
        try {
            DateTimeParser.DateTimeResult result = DateTimeParser.parseDateTime(by);
            Object[] timeData = {result.getDateTime(), result.hasTime()};
            return new ParseResult(CommandType.DEADLINE, new String[]{description}, timeData);
        } catch (DateTimeParseException e) {
            return new ParseResult(BotMessages.INVALID_DATE_FORMAT);
        }
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"deadline"};
    }
}