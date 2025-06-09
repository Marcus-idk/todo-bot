package org.todobot.parsers;

import java.util.Set;

public class ByeParser extends CommandParser {
    private static final Set<String> BYE_COMMANDS = Set.of(
        "bye", "exit", "quit", "goodbye", "done"
    );
    
    public static boolean canHandle(String command) {
        return BYE_COMMANDS.contains(command.toLowerCase());
    }
    
    @Override
    public ParseResult parse(String arguments) {
        // All bye command variants map to canonical "bye" command
        return new ParseResult("bye", new String[0]);
    }
}