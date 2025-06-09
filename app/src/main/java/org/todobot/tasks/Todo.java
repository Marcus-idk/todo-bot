package org.todobot.tasks;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return TaskTypeIcon.TODO.getIcon();
    }

    @Override
    public String getDetailsString() {
        return "";
    }
}