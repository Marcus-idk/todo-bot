package org.todobot.parsers;

import org.todobot.common.CommandType;

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