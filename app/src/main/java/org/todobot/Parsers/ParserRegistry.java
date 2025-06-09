package org.todobot.parsers;

import java.util.HashMap;
import java.util.Map;

public class ParserRegistry {
    private static final Map<String, CommandParser> parsers = new HashMap<>();
    
    static {
        // Register parsers - each declares its own keywords
        registerParser(new TodoParser());
        registerParser(new DeadlineParser());
        registerParser(new EventParser());
        registerParser(new MarkParser());
        registerParser(new UnmarkParser());
        registerParser(new ListParser());
        registerParser(new ByeParser());
    }
    
    private static void registerParser(CommandParser parser) {
        for (String keyword : parser.getCommandKeywords()) {
            parsers.put(keyword, parser);
        }
    }
    
    public static CommandParser getParser(String command) {
        return parsers.get(command);
    }
    
    public static boolean hasParser(String command) {
        return parsers.containsKey(command);
    }
}