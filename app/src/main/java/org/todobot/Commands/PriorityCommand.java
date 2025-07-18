package org.todobot.commands;

import org.todobot.common.BotMessages;
import org.todobot.model.Priority;
import org.todobot.model.Task;
import org.todobot.parsers.ParseResult;
import org.todobot.service.TaskList;

public class PriorityCommand extends Command {

    public PriorityCommand(TaskList taskList) {
        super(taskList);
    }

    @Override
    public String execute(String[] arguments) {
        throw new UnsupportedOperationException("PriorityCommand requires a ParseResult object.");
    }

    @Override
    public String execute(ParseResult parseResult) {
        String[] arguments = parseResult.getArguments();
        int taskNumber = Integer.parseInt(arguments[0]);
        Task task = taskList.getTask(taskNumber);
        if (task == null) return BotMessages.INVALID_TASK_NUMBER;

        task.setPriority(parsePriority(arguments[1]));
        return BotMessages.formatPriorityChanged(task);
    }

    private Priority parsePriority(String token) {
        return switch (token.toLowerCase()) {
            case "high", "h" -> Priority.HIGH;
            case "low", "l" -> Priority.LOW;
            default -> Priority.MEDIUM;
        };
    }
}
