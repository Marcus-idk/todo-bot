package org.todobot.parsers;

import org.todobot.BotMessages;
import org.todobot.CommandType;

public class UnmarkParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        try {
            int taskNumber = Integer.parseInt(arguments.trim());
            return new ParseResult(CommandType.UNMARK, new String[]{String.valueOf(taskNumber)});
        } catch (NumberFormatException e) {
            return new ParseResult(BotMessages.INVALID_NUMBER_FORMAT);
        }
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"unmark"};
    }
}