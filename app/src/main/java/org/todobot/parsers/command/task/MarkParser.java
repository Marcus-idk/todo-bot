package org.todobot.parsers.command.task;

import org.todobot.common.CommandType;
import org.todobot.parsers.util.NumericParser;

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