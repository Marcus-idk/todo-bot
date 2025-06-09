package org.todobot.parsers;

import java.util.HashMap;
import java.util.Map;

public class ParserRegistry {
    private static final Map<String, CommandParser> parsers = new HashMap<>();
    
    static {
        // Task creation parsers
        registerParser("todo", new TodoParser());
        registerParser("deadline", new DeadlineParser());
        registerParser("event", new EventParser());
        
        // Task management parsers
        registerParser("mark", new MarkUnmarkParser("mark"));
        registerParser("unmark", new MarkUnmarkParser("unmark"));
        
        // List command parser - register all variants
        ListParser listParser = new ListParser();
        registerParser("list", listParser);
        registerParser("ls", listParser);
        registerParser("show", listParser);
        registerParser("display", listParser);
        
        // Bye command parser - register all variants
        ByeParser byeParser = new ByeParser();
        registerParser("bye", byeParser);
        registerParser("exit", byeParser);
        registerParser("quit", byeParser);
        registerParser("goodbye", byeParser);
        registerParser("done", byeParser);
    }
    
    public static void registerParser(String command, CommandParser parser) {
        parsers.put(command, parser);
    }
    
    public static CommandParser getParser(String command) {
        return parsers.get(command);
    }
    
    public static boolean hasParser(String command) {
        return parsers.containsKey(command);
    }
}