package org.todobot.parsers;

import org.todobot.common.CommandType;

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