package org.todobot.tasks;

public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description);
        if (by == null) {
            throw new IllegalArgumentException("Deadline by date cannot be null");
        }
        if (by.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline by date cannot be empty");
        }
        this.by = by;
    }
    
    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String getDetailsString() {
        return " (by: " + by + ")";
    }

    public String getBy() {
        return by;
    }
}