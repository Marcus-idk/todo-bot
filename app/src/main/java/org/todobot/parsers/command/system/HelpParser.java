package org.todobot.parsers.command.system;

import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;

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