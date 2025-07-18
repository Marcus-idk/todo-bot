package org.todobot.parsers.command.system;

import org.todobot.common.CommandType;
import org.todobot.parsers.core.CommandParser;
import org.todobot.parsers.core.ParseResult;

public class ByeParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        // All bye command variants map to canonical "bye" command
        return new ParseResult(CommandType.BYE, new String[0]);
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"bye", "exit", "quit", "goodbye"};
    }
}