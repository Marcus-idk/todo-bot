package org.todobot.parsers;

import org.todobot.BotMessages;

public class TodoParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        if (arguments.trim().isEmpty()) {
            return new ParseResult(BotMessages.INVALID_TODO_FORMAT);
        }
        return new ParseResult("todo", new String[]{arguments});
    }
}