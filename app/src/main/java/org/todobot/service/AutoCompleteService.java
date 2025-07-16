package org.todobot.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.todobot.parsers.ByeParser;
import org.todobot.parsers.CommandParser;
import org.todobot.parsers.DeadlineParser;
import org.todobot.parsers.DeleteParser;
import org.todobot.parsers.EventParser;
import org.todobot.parsers.FindParser;
import org.todobot.parsers.HelpParser;
import org.todobot.parsers.ListParser;
import org.todobot.parsers.MarkParser;
import org.todobot.parsers.ToDoParser;
import org.todobot.parsers.UnmarkParser;

public class AutoCompleteService {
    private final Set<String> availableCommands;
    
    // Initialize service and load all available commands
    public AutoCompleteService() {
        this.availableCommands = new HashSet<>();
        loadCommandsFromRegistry();
    }
    
    // Extract all command keywords from parsers
    private void loadCommandsFromRegistry() {
        CommandParser[] parsers = {
            new ToDoParser(),
            new DeadlineParser(),
            new EventParser(),
            new MarkParser(),
            new UnmarkParser(),
            new DeleteParser(),
            new FindParser(),
            new ListParser(),
            new HelpParser(),
            new ByeParser()
        };
        
        for (CommandParser parser : parsers) {
            for (String keyword : parser.getCommandKeywords()) {
                availableCommands.add(keyword.toLowerCase());
            }
        }
    }
    
    // Get copy of all available commands
    public Set<String> getAllCommands() {
        return new HashSet<>(availableCommands);
    }
    
    // Find all commands that start with input
    public List<String> getMatchingCommands(String input) {
        if (input == null || input.trim().isEmpty()) {
            return List.of();
        }
        
        String lowerInput = input.toLowerCase().trim();
        return availableCommands.stream()
                .filter(command -> command.startsWith(lowerInput))
                .sorted()
                .collect(Collectors.toList());
    }
    
    // Get completion text for input (remainder of first match)
    public Optional<String> getBestCompletion(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Optional.empty();
        }
        
        String lowerInput = input.toLowerCase().trim();
        
        // No completion needed for exact match
        if (availableCommands.contains(lowerInput)) {
            return Optional.empty();
        }
        
        // Return remainder of first matching command
        return availableCommands.stream()
                .filter(command -> command.startsWith(lowerInput))
                .sorted()
                .findFirst()
                .map(command -> command.substring(lowerInput.length()));
    }
}