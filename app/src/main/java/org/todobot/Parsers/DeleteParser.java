package org.todobot.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;

public class DeleteParser extends CommandParser {
    private static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile("^\\s*([1-9]\\d*)\\s*$");
    
    @Override
    public ParseResult parse(String arguments) {
        // Check if it's the "all confirm" keyword
        if (arguments.trim().equalsIgnoreCase("all confirm")) {
            return new ParseResult(CommandType.DELETE_ALL_CONFIRM, new String[]{});
        }
        
        // Check if it's the "all" keyword
        if (arguments.trim().equalsIgnoreCase("all")) {
            return new ParseResult(CommandType.DELETE_ALL, new String[]{});
        }
        
        // Check if it's a positive integer (existing behavior)
        Matcher matcher = POSITIVE_INTEGER_PATTERN.matcher(arguments);
        if (matcher.matches()) {
            String taskNumber = matcher.group(1);
            return new ParseResult(CommandType.DELETE, new String[]{taskNumber});
        }
        
        // Invalid format
        return new ParseResult(BotMessages.INVALID_NUMBER_FORMAT);
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"delete", "del", "d", "remove", "rm"};
    }
}