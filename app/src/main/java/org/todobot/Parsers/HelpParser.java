package org.todobot.parsers;

import org.todobot.common.CommandType;

public class HelpParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        return new ParseResult(CommandType.HELP, new String[0]);
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"help", "h", "?", "commands"};
    }
}