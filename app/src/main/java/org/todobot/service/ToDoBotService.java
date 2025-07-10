package org.todobot.service;

import org.todobot.commands.AddCommand;
import org.todobot.commands.Command;
import org.todobot.commands.DeleteCommand;
import org.todobot.commands.FindCommand;
import org.todobot.commands.HelpCommand;
import org.todobot.commands.ListCommand;
import org.todobot.commands.MarkCommand;
import org.todobot.common.CommandType;
import org.todobot.parsers.ParseResult;
import org.todobot.parsers.Parser;
import org.todobot.storage.TaskStorage;

public class ToDoBotService {
    private final TaskList taskList;
    private final TaskStorage storage;
    private boolean textInputMode = false; // Default is button mode
    
    public ToDoBotService() {
        this.storage = new TaskStorage();
        this.taskList = new TaskList();
        
        // Load tasks on startup
        taskList.setTasks(storage.loadTasks());
    }
    
    public String processCommand(String input) {
        ParseResult result = Parser.parse(input);
        
        if (!result.isValid()) {
            return getResponseForMode(result.getErrorMessage());
        }
        
        // Handle bye command
        if (result.getCommandType() == CommandType.BYE) {
            return " Bye. Hope to see you again soon!";
        }
        
        Command command = createCommand(result.getCommandType());
        if (command == null) {
            return getResponseForMode(" Unknown command type.");
        }
        
        String output = command.execute(result);
        
        // Save tasks after any command that might modify them
        if (isModifyingCommand(result.getCommandType())) {
            storage.saveTasks(taskList.getAllTasks());
        }
        
        return getResponseForMode(output);
    }
    
    private String getResponseForMode(String baseResponse) {
        if (textInputMode) {
            return baseResponse; // Input-oriented response
        } else {
            return getButtonOrientedResponse(baseResponse); // Button-oriented response
        }
    }
    
    private String getButtonOrientedResponse(String baseResponse) {
        // For now, return the same response but we'll enhance this later
        // This is where we'll add button logic
        return baseResponse;
    }
    
    public void setTextInputMode(boolean textInputMode) {
        this.textInputMode = textInputMode;
    }
    
    public boolean isTextInputMode() {
        return textInputMode;
    }
    
    public String processButtonClick(String buttonAction) {
        if (buttonAction == null || buttonAction.isEmpty()) {
            return getMainMenu();
        }
        
        switch (buttonAction) {
            case "add_task":
                return "Choose task type:|todo,deadline,event,back";
            case "todo":
                return "Enter task description:";
            case "deadline":
                return "Enter deadline task details:|DEADLINE_FORM";
            case "event":
                return "Enter event details:|EVENT_FORM";
            case "view_tasks":
                return getTaskListWithButtons();
            case "find_tasks":
                return "Enter search term:";
            case "help":
                return getHelpWithButtons();
            case "back":
            case "main_menu":
                return getMainMenu();
            default:
                // Handle task-specific actions like "mark_1", "delete_1"
                return handleTaskAction(buttonAction);
        }
    }
    
    private String getMainMenu() {
        return "What would you like to do?|add_task,view_tasks,find_tasks,help,exit";
    }
    
    private String getTaskListWithButtons() {
        if (taskList.isEmpty()) {
            return "No tasks found.|back";
        }
        
        StringBuilder response = new StringBuilder("Here are your tasks:\n");
        for (int i = 0; i < taskList.getTaskCount(); i++) {
            response.append((i + 1)).append(". ").append(taskList.getTask(i + 1).toString()).append("\n");
        }
        
        // Use special format for dropdown: DROPDOWN|taskCount|back
        return response.toString() + "|DROPDOWN|" + taskList.getTaskCount() + "|back";
    }
    
    private String getHelpWithButtons() {
        return "Available actions:\n" +
               "• Add Task: Create todo, deadline, or event tasks\n" +
               "• View Tasks: See all your tasks with mark/unmark/delete options\n" +
               "• Find Tasks: Search for tasks by keyword\n" +
               "• Toggle (▲/▼): Switch between button mode and text input mode\n" +
               "\nText commands: todo <task>, deadline <task> /by <date> <time>, event <task> /from <date> <time> /to <date> <time>, list, find <keyword>, mark <num>, unmark <num>, delete <num>, bye\n" +
               "|back";
    }
    
    private String handleTaskAction(String buttonAction) {
        if (buttonAction.startsWith("mark_")) {
            int taskNumber = Integer.parseInt(buttonAction.substring(5));
            if (taskNumber >= 1 && taskNumber <= taskList.getTaskCount()) {
                String command = "mark " + taskNumber;
                return processCommand(command) + "|back";
            }
        } else if (buttonAction.startsWith("delete_")) {
            int taskNumber = Integer.parseInt(buttonAction.substring(7));
            if (taskNumber >= 1 && taskNumber <= taskList.getTaskCount()) {
                String command = "delete " + taskNumber;
                return processCommand(command) + "|back";
            }
        }
        return "Invalid action.|back";
    }
    
    private Command createCommand(CommandType commandType) {
        return switch (commandType) {
            case TODO, DEADLINE, EVENT -> new AddCommand(taskList, commandType);
            case LIST -> new ListCommand(taskList);
            case MARK -> new MarkCommand(taskList, true);
            case UNMARK -> new MarkCommand(taskList, false);
            case DELETE -> new DeleteCommand(taskList);
            case FIND -> new FindCommand(taskList);
            case HELP -> new HelpCommand(taskList);
            default -> null;
        };
    }
    
    private boolean isModifyingCommand(CommandType commandType) {
        return commandType == CommandType.TODO ||
               commandType == CommandType.DEADLINE ||
               commandType == CommandType.EVENT ||
               commandType == CommandType.MARK ||
               commandType == CommandType.UNMARK ||
               commandType == CommandType.DELETE;
    }
    
    public boolean shouldExit(String input) {
        ParseResult result = Parser.parse(input);
        return result.isValid() && result.getCommandType() == CommandType.BYE;
    }
    
    public void cleanup() {
        // Final save before exit
        storage.saveTasks(taskList.getAllTasks());
    }
}