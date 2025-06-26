package org.todobot.model;

import java.time.LocalDateTime;
import org.todobot.parsers.DateTimeParser;

public class Deadline extends Task {
    private LocalDateTime by;
    private boolean hasTime;

    public Deadline(String description, LocalDateTime by, boolean hasTime) {
        super(description);
        if (by == null) {
            throw new IllegalArgumentException("Deadline by date cannot be null");
        }
        this.by = by;
        this.hasTime = hasTime;
    }
    
    @Override
    public String getTypeIcon() {
        return TaskTypeIcon.DEADLINE.getIcon();
    }

    @Override
    public String getDetailsString() {
        return " (by: " + DateTimeParser.formatDateTime(by, hasTime) + ")";
    }

    public String getBy() {
        return DateTimeParser.formatDateTime(by, hasTime);
    }
    
    public LocalDateTime getByDateTime() {
        return by;
    }
    
    public boolean hasTimeInfo() {
        return hasTime;
    }
}