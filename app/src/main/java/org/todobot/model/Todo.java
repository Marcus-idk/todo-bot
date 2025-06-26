package org.todobot.model;

public class ToDo extends Task {
    public ToDo(String description) {
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