package org.todobot.parsers;

public abstract class CommandParser {
    public abstract ParseResult parse(String arguments);
}