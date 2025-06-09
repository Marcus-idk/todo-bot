package org.todobot.parsers;

import org.todobot.BotMessages;

public class MarkParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        try {
            int taskNumber = Integer.parseInt(arguments.trim());
            return new ParseResult("mark", new String[]{String.valueOf(taskNumber)});
        } catch (NumberFormatException e) {
            return new ParseResult(BotMessages.INVALID_NUMBER_FORMAT);
        }
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"mark"};
    }
}