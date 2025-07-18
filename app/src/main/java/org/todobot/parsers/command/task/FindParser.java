package org.todobot.parsers.command.task;

import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;

public class FindParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        String keyword = arguments.trim();
        return new ParseResult(CommandType.FIND, new String[]{keyword});
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"find", "search"};
    }
}