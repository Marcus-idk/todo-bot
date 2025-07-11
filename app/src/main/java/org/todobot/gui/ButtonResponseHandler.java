package org.todobot.gui;

import org.todobot.service.TaskList;

public class ButtonResponseHandler {
    
    public interface CommandProcessor {
        String processCommand(String command);
    }
    
    private final CommandProcessor commandProcessor;
    private final TaskList taskList;
    
    public ButtonResponseHandler(CommandProcessor commandProcessor, TaskList taskList) {
        this.commandProcessor = commandProcessor;
        this.taskList = taskList;
    }
    
    public String processButtonClick(String buttonAction) {
        if (buttonAction == null || buttonAction.isEmpty()) {
            return getMainMenu();
        }
        
        return switch (buttonAction) {
            case "add_task" -> "Choose task type:|todo,deadline,event,back";
            case "todo" -> "Enter task description:";
            case "deadline" -> "Enter deadline task details:|DEADLINE_FORM";
            case "event" -> "Enter event details:|EVENT_FORM";
            case "view_tasks" -> getTaskListWithButtons();
            case "find_tasks" -> "Enter search term:";
            case "help" -> getHelpWithButtons();
            case "back", "main_menu" -> getMainMenu();
            default -> handleTaskAction(buttonAction);
        };
    }
    
    private String getMainMenu() {
        return "What would you like to do?|add_task,view_tasks,find_tasks,help,exit";
    }
    
    private String getTaskListWithButtons() {
        // Use existing ListCommand
        String taskListOutput = commandProcessor.processCommand("list");
        
        if (taskList.isEmpty()) {
            return taskListOutput + "|back";
        }
        
        // Get task count directly from TaskList - no parsing needed
        int taskCount = taskList.getTaskCount();
        
        // Use special format for dropdown: DROPDOWN|taskCount|back
        return taskListOutput + "|DROPDOWN|" + taskCount + "|back";
    }
    
    private String getHelpWithButtons() {
        // Use existing HelpCommand
        String help = commandProcessor.processCommand("help");
        
        // Add GUI-specific help
        String guiHelp = "\n GUI-specific features:\n" +
                        "   • Toggle (▲/▼): Switch between button mode and text input mode\n" +
                        "   • Use buttons or forms for easier task creation";
        
        return help + guiHelp + "|back";
    }
    
    public String handleDropdownSelection(String selectedTask, String selectedAction) {
        // Extract task number from "Task 1" format - let parser handle validation
        String taskNumber = selectedTask.split(" ")[1];
        
        // Convert action to command
        String command;
        switch (selectedAction.toLowerCase()) {
            case "mark":
                command = "mark " + taskNumber;
                break;
            case "unmark":
                command = "unmark " + taskNumber;
                break;
            case "delete":
                command = "delete " + taskNumber;
                break;
            default:
                return "Unknown action: " + selectedAction;
        }
        
        // Process the command - parser will handle number validation
        return commandProcessor.processCommand(command);
    }
    
    public String buildTaskCommand(String taskType, String taskText) {
        // Command format logic belongs in business layer
        return switch (taskType) {
            case "todo" -> "todo " + taskText;
            case "find_tasks" -> "find " + taskText;
            case "deadline" -> taskText; // User should type full format
            case "event" -> taskText; // User should type full format
            default -> taskText;
        };
    }
    
    private String handleTaskAction(String buttonAction) {
        // Let parser and existing commands handle all validation and error checking
        if (buttonAction.startsWith("mark_")) {
            String command = "mark " + buttonAction.substring(5);
            return commandProcessor.processCommand(command) + "|back";
        } else if (buttonAction.startsWith("unmark_")) {
            String command = "unmark " + buttonAction.substring(7);
            return commandProcessor.processCommand(command) + "|back";
        } else if (buttonAction.startsWith("delete_")) {
            String command = "delete " + buttonAction.substring(7);
            return commandProcessor.processCommand(command) + "|back";
        }
        return "Invalid action.|back";
    }
}