package org.todobot.parsers.command.task;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;

public class ToDoParser extends CommandParser {
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