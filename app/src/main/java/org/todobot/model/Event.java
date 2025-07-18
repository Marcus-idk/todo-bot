package org.todobot.model;

import java.time.LocalDateTime;

import org.todobot.parsers.util.DateTimeParser;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private boolean hasFromTime;
    private boolean hasToTime;

    public Event(String description, LocalDateTime from, boolean hasFromTime, LocalDateTime to, boolean hasToTime) {
        super(description);
        if (from == null) {
            throw new IllegalArgumentException("Event from time cannot be null");
        }
        if (to == null) {
            throw new IllegalArgumentException("Event to time cannot be null");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Event start time must be before end time");
        }
        this.from = from;
        this.hasFromTime = hasFromTime;
        this.to = to;
        this.hasToTime = hasToTime;
    }

    @Override
    public String getTypeIcon() {
        return TaskTypeIcon.EVENT.getIcon();
    }

    @Override
    public String getDetailsString() {
        return " (from: " + DateTimeParser.formatDateTime(from, hasFromTime) + 
               " to: " + DateTimeParser.formatDateTime(to, hasToTime) + ")";
    }

    public String getFrom() {
        return DateTimeParser.formatDateTime(from, hasFromTime);
    }

    public String getTo() {
        return DateTimeParser.formatDateTime(to, hasToTime);
    }
    
    public LocalDateTime getFromDateTime() {
        return from;
    }
    
    public LocalDateTime getToDateTime() {
        return to;
    }
    
    public boolean hasFromTime() {
        return hasFromTime;
    }
    
    public boolean hasToTime() {
        return hasToTime;
    }
}