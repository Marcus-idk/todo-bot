package org.todobot.parsers;

import org.todobot.common.CommandType;

public class DeleteParser extends NumericParser {
    @Override
    protected CommandType getCommandType() {
        return CommandType.DELETE;
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"delete", "del", "d", "remove", "rm"};
    }
}