package org.todobot.parsers.core;
import org.todobot.common.BotMessages;

public class Parser {
    public static ParseResult parse(String input) {
        if (input == null) {
            return new ParseResult(BotMessages.EMPTY_INPUT);
        }
        
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return new ParseResult(BotMessages.EMPTY_INPUT);
        }
        
        String[] parts = trimmed.split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";
        
        // Delegate all commands to registered parsers
        CommandParser parser = ParserRegistry.getParser(command);
        if (parser != null) {
            return parser.parse(arguments);
        }
        
        // Unknown command
        return new ParseResult(BotMessages.INVALID_COMMAND);
    }
}