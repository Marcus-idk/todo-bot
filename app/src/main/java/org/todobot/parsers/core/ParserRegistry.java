package org.todobot.parsers.core;

import java.util.HashMap;
import java.util.Map;

import org.todobot.parsers.command.system.ByeParser;
import org.todobot.parsers.command.system.HelpParser;
import org.todobot.parsers.command.task.DeadlineParser;
import org.todobot.parsers.command.task.DeleteParser;
import org.todobot.parsers.command.task.EventParser;
import org.todobot.parsers.command.task.FindParser;
import org.todobot.parsers.command.task.ListParser;
import org.todobot.parsers.command.task.MarkParser;
import org.todobot.parsers.command.task.PriorityParser;
import org.todobot.parsers.command.task.ToDoParser;
import org.todobot.parsers.command.task.UnmarkParser;

public class ParserRegistry {
    private static final Map<String, CommandParser> parsers = new HashMap<>();
    
    static {
        registerParser(new ToDoParser());
        registerParser(new DeadlineParser());
        registerParser(new EventParser());
        registerParser(new MarkParser());
        registerParser(new UnmarkParser());
        registerParser(new DeleteParser());
        registerParser(new FindParser());
        registerParser(new ListParser());
        registerParser(new HelpParser());
        registerParser(new ByeParser());
        registerParser(new PriorityParser());
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