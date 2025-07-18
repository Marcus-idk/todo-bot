package org.todobot.parsers.command.task;

import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;

public class ListParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        return new ParseResult(CommandType.LIST, new String[0]);
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"list", "ls", "show", "display"};
    }
}