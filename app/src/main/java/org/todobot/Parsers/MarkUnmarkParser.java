package org.todobot.parsers;

import org.todobot.BotMessages;

public class MarkUnmarkParser extends CommandParser {
    private final String commandType;
    
    public MarkUnmarkParser(String commandType) {
        this.commandType = commandType;
    }
    
    @Override
    public ParseResult parse(String arguments) {
        try {
            int taskNumber = Integer.parseInt(arguments.trim());
            return new ParseResult(commandType, new String[]{String.valueOf(taskNumber)});
        } catch (NumberFormatException e) {
            return new ParseResult(BotMessages.INVALID_NUMBER_FORMAT);
        }
    }
}