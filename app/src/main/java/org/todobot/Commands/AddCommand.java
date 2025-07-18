package org.todobot.commands;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;
import org.todobot.model.Deadline;
import org.todobot.model.Event;
import org.todobot.model.Priority;
import org.todobot.model.Task;
import org.todobot.model.ToDo;
import org.todobot.parsers.ParseResult;
import org.todobot.service.TaskList;

public class AddCommand extends Command {

    private final CommandType taskType;
    private static final Pattern PRIORITY_PATTERN = Pattern.compile("!([a-zA-Z]+)");

    private record DescriptionInfo(String description, Priority priority) {}

    public AddCommand(TaskList taskList, CommandType taskType) {
        super(taskList);
        this.taskType = taskType;
    }

    @Override
    public String execute(String[] arguments) {
        throw new UnsupportedOperationException("AddCommand requires a ParseResult object.");
    }

    @Override
    public String execute(ParseResult parseResult) {
        if (taskList.isFull()) {
            return BotMessages.TASK_LIMIT_REACHED;
        }

        DescriptionInfo info = processDescription(parseResult.getArguments()[0]);

        try {
            Task task = createTask(info.description(), parseResult.getTimeData());
            task.setPriority(info.priority());
            taskList.addTask(task);
            return BotMessages.formatAddedTask(task, taskList.getTaskCount());
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private Task createTask(String description, Object[] timeData) {
        return switch (taskType) {
            case TODO -> new ToDo(description);
            case DEADLINE -> createDeadline(description, timeData);
            case EVENT -> createEvent(description, timeData);
            default -> throw new IllegalArgumentException("Unexpected value: " + taskType);
        };
    }

    private Deadline createDeadline(String description, Object[] timeData) {
        if (timeData == null || timeData.length < 2
                || !(timeData[0] instanceof LocalDateTime)
                || !(timeData[1] instanceof Boolean)) {
            throw new IllegalArgumentException(BotMessages.INVALID_DATE_FORMAT);
        }
        return new Deadline(description, (LocalDateTime) timeData[0], (Boolean) timeData[1]);
    }

    private Event createEvent(String description, Object[] timeData) {
        if (timeData == null || timeData.length < 4
                || !(timeData[0] instanceof LocalDateTime) || !(timeData[1] instanceof Boolean)
                || !(timeData[2] instanceof LocalDateTime) || !(timeData[3] instanceof Boolean)) {
            throw new IllegalArgumentException(BotMessages.INVALID_DATE_FORMAT);
        }
        try {
            return new Event(description,
                    (LocalDateTime) timeData[0], (Boolean) timeData[1],
                    (LocalDateTime) timeData[2], (Boolean) timeData[3]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(BotMessages.INVALID_EVENT_TIME_ORDER, e);
        }
    }

    private static DescriptionInfo processDescription(String fullDescription) {
        if (fullDescription == null || fullDescription.isBlank()) {
            return new DescriptionInfo("", Priority.MEDIUM);
        }

        Matcher matcher = PRIORITY_PATTERN.matcher(fullDescription);
        Priority priority = Priority.MEDIUM;

        if (matcher.find()) {
            priority = switch (matcher.group(1).toLowerCase()) {
                case "high", "h" -> Priority.HIGH;
                case "low", "l" -> Priority.LOW;
                default -> Priority.MEDIUM;
            };
        }

        String description = fullDescription.replaceFirst("![a-zA-Z]+", " ")
                                             .replaceAll("\\s+", " ")
                                             .trim();

        return new DescriptionInfo(description, priority);
    }
}