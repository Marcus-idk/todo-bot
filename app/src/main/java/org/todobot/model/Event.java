package org.todobot.model;

public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        if (from == null) {
            throw new IllegalArgumentException("Event from time cannot be null");
        }
        if (from.trim().isEmpty()) {
            throw new IllegalArgumentException("Event from time cannot be empty");
        }
        if (to == null) {
            throw new IllegalArgumentException("Event to time cannot be null");
        }
        if (to.trim().isEmpty()) {
            throw new IllegalArgumentException("Event to time cannot be empty");
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return TaskTypeIcon.EVENT.getIcon();
    }

    @Override
    public String getDetailsString() {
        return " (from: " + from + " to: " + to + ")";
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}