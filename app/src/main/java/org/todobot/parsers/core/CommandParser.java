package org.todobot.parsers.core;

public abstract class CommandParser {
    public abstract ParseResult parse(String arguments);
    public abstract String[] getCommandKeywords();
}