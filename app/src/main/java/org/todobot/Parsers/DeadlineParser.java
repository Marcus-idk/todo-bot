package org.todobot.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.todobot.BotMessages;
import org.todobot.CommandType;

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
        
        return new ParseResult(CommandType.DEADLINE, new String[]{description, by});
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"deadline"};
    }
}