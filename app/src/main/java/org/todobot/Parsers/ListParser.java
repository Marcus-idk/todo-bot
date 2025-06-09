package org.todobot.parsers;

import java.util.Set;

public class ListParser extends CommandParser {
    private static final Set<String> LIST_COMMANDS = Set.of(
        "list", "ls", "show", "display"
    );
    
    public static boolean canHandle(String command) {
        return LIST_COMMANDS.contains(command.toLowerCase());
    }
    
    @Override
    public ParseResult parse(String arguments) {
        // All list command variants map to canonical "list" command
        return new ParseResult("list", new String[0]);
    }
}