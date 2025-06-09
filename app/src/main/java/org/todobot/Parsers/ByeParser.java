package org.todobot.parsers;

public class ByeParser extends CommandParser {
    @Override
    public ParseResult parse(String arguments) {
        // All bye command variants map to canonical "bye" command
        return new ParseResult("bye", new String[0]);
    }
    
    @Override
    public String[] getCommandKeywords() {
        return new String[]{"bye", "exit", "quit", "goodbye", "done"};
    }
}