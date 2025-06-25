
package org.todobot.commands;

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
        
        if (isMarkOperation) {
            if (!taskList.markTask(taskNumber)) {
                return BotMessages.INVALID_TASK_NUMBER;
            } else {
                Task task = taskList.getTask(taskNumber);
                return BotMessages.formatMarkedTask(task);
            }
        } else {
            if (!taskList.unmarkTask(taskNumber)) {
                return BotMessages.INVALID_TASK_NUMBER;
            } else {
                Task task = taskList.getTask(taskNumber);
                return BotMessages.formatUnmarkedTask(task);
            }
        }
    }
}