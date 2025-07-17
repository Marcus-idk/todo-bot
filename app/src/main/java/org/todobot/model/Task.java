package org.todobot.model;

public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected Priority priority;

    public Task(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Task description cannot be null");
        }
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }
        this.description = description;
        this.isDone = false;
        this.priority = Priority.MEDIUM;
    }

    public String getStatusIcon() {
        return (isDone ? TaskStatusIcon.DONE.getIcon() : TaskStatusIcon.NOT_DONE.getIcon());
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.priority = priority;
    }

    public String getPriorityIcon() {
        return priority.getIcon();
    }

    // Abstract methods
    public abstract String getTypeIcon();
    public abstract String getDetailsString();

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "][" + getPriorityIcon() + "] " + description + getDetailsString();
    }
}