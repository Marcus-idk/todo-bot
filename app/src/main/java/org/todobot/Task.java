package org.todobot;
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Task description cannot be null");
        }
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}