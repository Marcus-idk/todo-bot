package org.todobot.parsers;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;

public class TodoParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        if (arguments.trim().isEmpty()) {
            return new ParseResult(BotMessages.INVALID_TODO_FORMAT);
        }
        return new ParseResult(CommandType.TODO, new String[]{arguments});
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"todo"};
    }
}