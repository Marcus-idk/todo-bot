package org.todobot.gui;

import java.time.LocalDate;

import org.todobot.parsers.DateTimeParser;
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
        String taskNumber = selectedTask.split(" ")[1];
        
        String command;
        switch (selectedAction.toLowerCase()) {
            case "mark":
                command = buildTaskCommand("mark", taskNumber);
                break;
            case "unmark":
                command = buildTaskCommand("unmark", taskNumber);
                break;
            case "delete":
                command = buildTaskCommand("delete", taskNumber);
                break;
            default:
                return "Unknown action: " + selectedAction;
        }
        
        return commandProcessor.processCommand(command);
    }
    
    public String buildTaskCommand(String taskType, Object... args) {
        return switch (taskType) {
            case "todo" -> "todo " + (String) args[0];
            case "find_tasks" -> "find " + (String) args[0];
            case "mark" -> "mark " + (String) args[0];
            case "unmark" -> "unmark " + (String) args[0];
            case "delete" -> "delete " + (String) args[0];
            case "deadline" -> {
                String description = (String) args[0];
                LocalDate date = (LocalDate) args[1];
                String hour = (String) args[2];
                String minute = (String) args[3];
                String dateTimeStr = DateTimeParser.formatForCommandInput(date, hour, minute);
                String command = description + " /by " + dateTimeStr;
                yield "deadline " + command;
            }
            case "event" -> {
                String description = (String) args[0];
                LocalDate fromDate = (LocalDate) args[1];
                String fromHour = (String) args[2];
                String fromMinute = (String) args[3];
                LocalDate toDate = (LocalDate) args[4];
                String toHour = (String) args[5];
                String toMinute = (String) args[6];
                String fromDateTimeStr = DateTimeParser.formatForCommandInput(fromDate, fromHour, fromMinute);
                String toDateTimeStr = DateTimeParser.formatForCommandInput(toDate, toHour, toMinute);
                String command = description + " /from " + fromDateTimeStr + " /to " + toDateTimeStr;
                yield "event " + command;
            }
            default -> (String) args[0];
        };
    }
    
    private String handleTaskAction(String buttonAction) {
        if (buttonAction.startsWith("mark_")) {
            String command = buildTaskCommand("mark", buttonAction.substring(5));
            return commandProcessor.processCommand(command) + "|back";
        } else if (buttonAction.startsWith("unmark_")) {
            String command = buildTaskCommand("unmark", buttonAction.substring(7));
            return commandProcessor.processCommand(command) + "|back";
        } else if (buttonAction.startsWith("delete_")) {
            String command = buildTaskCommand("delete", buttonAction.substring(7));
            return commandProcessor.processCommand(command) + "|back";
        }
        return "Invalid action.|back";
    }
}