package org.todobot.Commands;
import org.todobot.BotMessages;
import org.todobot.Deadline;
import org.todobot.Event;
import org.todobot.Task;
import org.todobot.TaskList;
import org.todobot.Todo;

public class AddCommand extends Command {
    private final String taskType;
    
    public AddCommand(TaskList taskList, String taskType) {
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
            case "todo":
                task = new Todo(arguments[0]);
                break;
            case "deadline":
                task = new Deadline(arguments[0], arguments[1]);
                break;
            case "event":
                task = new Event(arguments[0], arguments[1], arguments[2]);
                break;
            default:
                return BotMessages.INVALID_COMMAND;
        }
        
        taskList.addTask(task);
        return BotMessages.formatAddedTask(task, taskList.getTaskCount());
    }
}