package org.todobot.parsers.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;

public abstract class NumericParser extends CommandParser {
    // Regex: Matches positive integers (1-9 followed by any digits), with optional whitespace at start/end
    private static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile("^\\s*([1-9]\\d*)\\s*$");
    
    @Override
    public ParseResult parse(String arguments) {
        Matcher matcher = POSITIVE_INTEGER_PATTERN.matcher(arguments);
        
        if (!matcher.matches()) {
            return new ParseResult(BotMessages.INVALID_NUMBER_FORMAT);
        }
        
        String taskNumber = matcher.group(1);
        return new ParseResult(getCommandType(), new String[]{taskNumber});
    }
    
    protected abstract CommandType getCommandType();
}