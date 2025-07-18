package org.todobot.parsers.command.task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;

public class PriorityParser extends CommandParser {
    private static final Pattern PRIORITY_PATTERN = Pattern.compile("^\\s*([1-9]\\d*)\\s+(!?[a-zA-Z]+)\\s*$");
    
    @Override
    public ParseResult parse(String arguments) {
        Matcher matcher = PRIORITY_PATTERN.matcher(arguments);
        
        if (!matcher.matches()) {
            return new ParseResult(BotMessages.INVALID_PRIORITY_FORMAT);
        }
        
        String taskNumber = matcher.group(1);
        String priorityToken = matcher.group(2);
        
        // Remove optional ! prefix from priority token
        if (priorityToken.startsWith("!")) {
            priorityToken = priorityToken.substring(1);
        }
        
        // Validate priority token
        String normalizedPriority = priorityToken.toLowerCase();
        if (!isValidPriorityToken(normalizedPriority)) {
            return new ParseResult(BotMessages.INVALID_PRIORITY_LEVEL);
        }
        
        return new ParseResult(CommandType.PRIORITY, new String[]{taskNumber, priorityToken});
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"priority"};
    }
    
    private boolean isValidPriorityToken(String token) {
        return token.equals("high") || token.equals("h") ||
               token.equals("medium") || token.equals("m") ||
               token.equals("low") || token.equals("l");
    }
}