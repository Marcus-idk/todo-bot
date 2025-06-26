package org.todobot.parsers;

import org.todobot.common.CommandType;

public class MarkParser extends NumericParser {
    @Override
    protected CommandType getCommandType() {
        return CommandType.MARK;
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"mark"};
    }
}