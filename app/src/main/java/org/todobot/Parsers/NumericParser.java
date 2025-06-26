package org.todobot.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;

public abstract class NumericParser extends CommandParser {
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