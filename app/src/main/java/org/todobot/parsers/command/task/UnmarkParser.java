package org.todobot.parsers.command.task;

import org.todobot.common.CommandType;
import org.todobot.parsers.util.NumericParser;

public class UnmarkParser extends NumericParser {
    @Override
    protected CommandType getCommandType() {
        return CommandType.UNMARK;
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"unmark"};
    }
}