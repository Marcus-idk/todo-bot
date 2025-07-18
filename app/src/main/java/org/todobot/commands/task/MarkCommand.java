package org.todobot.commands.task;

import org.todobot.commands.core.Command;
import org.todobot.common.BotMessages;
import org.todobot.model.Task;
import org.todobot.service.TaskList;

public class MarkCommand extends Command {
    private final boolean isMarkOperation;

    public MarkCommand(TaskList taskList, boolean isMarkOperation) {
        super(taskList);
        this.isMarkOperation = isMarkOperation;
    }

    @Override
    public String execute(String[] arguments) {
        int taskNumber = Integer.parseInt(arguments[0]);
        boolean success = isMarkOperation ? taskList.markTask(taskNumber) : taskList.unmarkTask(taskNumber);

        if (!success) return BotMessages.INVALID_TASK_NUMBER;

        Task task = taskList.getTask(taskNumber);
        return isMarkOperation ? BotMessages.formatMarkedTask(task) : BotMessages.formatUnmarkedTask(task);
    }
}
