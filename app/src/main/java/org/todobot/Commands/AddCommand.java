package org.todobot.commands;
import org.todobot.BotMessages;
import org.todobot.CommandType;
import org.todobot.TaskList;
import org.todobot.tasks.Deadline;
import org.todobot.tasks.Event;
import org.todobot.tasks.Task;
import org.todobot.tasks.Todo;

public class AddCommand extends Command {
    private final CommandType taskType;
    
    public AddCommand(TaskList taskList, CommandType taskType) {
        super(taskList);
        this.taskType = taskType;
    }
    
    @Override
    public String execute(String[] arguments) {
        if (taskList.isFull()) {
            return BotMessages.TASK_LIMIT_REACHED;
        }
        
        Task task;
        switch (taskType) {
            case TODO -> task = new Todo(arguments[0]);
            case DEADLINE -> task = new Deadline(arguments[0], arguments[1]);
            case EVENT -> task = new Event(arguments[0], arguments[1], arguments[2]);
            default -> {
                return BotMessages.INVALID_COMMAND;
            }
        }
        
        taskList.addTask(task);
        return BotMessages.formatAddedTask(task, taskList.getTaskCount());
    }
}