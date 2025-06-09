package org.todobot.parsers;

import org.todobot.CommandType;

public class ListParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        // All list command variants map to canonical "list" command
        return new ParseResult(CommandType.LIST, new String[0]);
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"list", "ls", "show", "display"};
    }
}