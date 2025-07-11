package org.todobot.service;

import java.time.LocalDate;

import org.todobot.commands.AddCommand;
import org.todobot.commands.Command;
import org.todobot.commands.DeleteCommand;
import org.todobot.commands.FindCommand;
import org.todobot.commands.HelpCommand;
import org.todobot.commands.ListCommand;
import org.todobot.commands.MarkCommand;
import org.todobot.common.CommandType;
import org.todobot.gui.BotResponse;
import org.todobot.parsers.DateTimeParser;
import org.todobot.parsers.ParseResult;
import org.todobot.parsers.Parser;
import org.todobot.storage.TaskStorage;

public class ToDoBotService {
    private final TaskList taskList;
    private final TaskStorage storage;
    private boolean shouldExit = false;
    
    public ToDoBotService() {
        this.storage = new TaskStorage();
        this.taskList = new TaskList();
        
        // Load tasks on startup
        taskList.setTasks(storage.loadTasks());
    }
    
    public String processCommand(String input) {
        ParseResult result = Parser.parse(input);
        
        if (!result.isValid()) {
            return result.getErrorMessage();
        }
        
        // Handle bye command
        if (result.getCommandType() == CommandType.BYE) {
            shouldExit = true;
            return " Bye. Hope to see you again soon!";
        }
        
        Command command = createCommand(result.getCommandType());
        if (command == null) {
            return " Unknown command type.";
        }
        
        String output = command.execute(result);
        
        // Save tasks after any command that might modify them
        if (isModifyingCommand(result.getCommandType())) {
            storage.saveTasks(taskList.getAllTasks());
        }
        
        return output;
    }
    
    public BotResponse processButtonClick(String buttonAction) {
        if (buttonAction == null || buttonAction.isEmpty()) {
            return getMainMenu();
        }
        
        return switch (buttonAction) {
            case "add_task" -> new BotResponse("Choose task type:", 
                java.util.List.of("todo", "deadline", "event", "back"));
            case "todo" -> new BotResponse("Enter task description:", BotResponse.FormType.TODO);
            case "deadline" -> new BotResponse("Enter deadline task details:", BotResponse.FormType.DEADLINE);
            case "event" -> new BotResponse("Enter event details:", BotResponse.FormType.EVENT);
            case "view_tasks" -> getTaskListWithButtons();
            case "find_tasks" -> new BotResponse("Enter search term:", BotResponse.FormType.FIND);
            case "help" -> getHelpWithButtons();
            case "back", "main_menu" -> getMainMenu();
            default -> handleTaskAction(buttonAction);
        };
    }
    
    public String handleDropdownSelection(String selectedTask, String selectedAction) {
        String taskNumber = selectedTask.split(" ")[1];
        
        String command;
        switch (selectedAction.toLowerCase()) {
            case "mark" -> command = buildTaskCommand("mark", taskNumber);
            case "unmark" -> command = buildTaskCommand("unmark", taskNumber);
            case "delete" -> command = buildTaskCommand("delete", taskNumber);
            default -> {
                return "Unknown action: " + selectedAction;
            }
        }
        
        return processCommand(command);
    }
    
    public String handleDeadlineTask(String description, LocalDate date, String hour, String minute) {
        String command = buildTaskCommand("deadline", description, date, hour, minute);
        return processCommand(command);
    }
    
    public String handleEventTask(String description, LocalDate fromDate, String fromHour, String fromMinute, LocalDate toDate, String toHour, String toMinute) {
        String command = buildTaskCommand("event", description, fromDate, fromHour, fromMinute, toDate, toHour, toMinute);
        return processCommand(command);
    }
    
    public String handleTodoTask(String taskText) {
        String command = buildTaskCommand("todo", taskText);
        return processCommand(command);
    }
    
    public String handleFindTask(String taskText) {
        String command = buildTaskCommand("find_tasks", taskText);
        return processCommand(command);
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
    
    public boolean shouldExit() {
        return shouldExit;
    }
    
    public void cleanup() {
        // Final save before exit
        storage.saveTasks(taskList.getAllTasks());
    }
    
    private BotResponse getMainMenu() {
        return new BotResponse("What would you like to do?", 
            java.util.List.of("add_task", "view_tasks", "find_tasks", "help", "exit"));
    }
    
    private BotResponse getTaskListWithButtons() {
        String taskListOutput = processCommand("list");
        
        if (taskList.isEmpty()) {
            return new BotResponse(taskListOutput, java.util.List.of("back"));
        }
        
        int taskCount = taskList.getTaskCount();
        return new BotResponse(taskListOutput, taskCount, "back");
    }
    
    private BotResponse getHelpWithButtons() {
        String help = processCommand("help");
        
        String guiHelp = """
        
                          GUI-specific features:
                            • Toggle (▲/▼): Switch between button mode and text input mode
                            • Use buttons or forms for easier task creation""";
        
        return new BotResponse(help + guiHelp, java.util.List.of("back"));
    }
    
    private BotResponse handleTaskAction(String buttonAction) {
        if (buttonAction.startsWith("mark_")) {
            String command = buildTaskCommand("mark", buttonAction.substring(5));
            String result = processCommand(command);
            return new BotResponse(result, java.util.List.of("back"));
        } else if (buttonAction.startsWith("unmark_")) {
            String command = buildTaskCommand("unmark", buttonAction.substring(7));
            String result = processCommand(command);
            return new BotResponse(result, java.util.List.of("back"));
        } else if (buttonAction.startsWith("delete_")) {
            String command = buildTaskCommand("delete", buttonAction.substring(7));
            String result = processCommand(command);
            return new BotResponse(result, java.util.List.of("back"));
        }
        return new BotResponse("Invalid action.", java.util.List.of("back"));
    }
    
    private String buildTaskCommand(String taskType, Object... args) {
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
}