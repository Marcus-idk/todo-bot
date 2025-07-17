package org.todobot.model;

public enum Priority {
    LOW("L"),
    MEDIUM("M"),
    HIGH("H");
    
    private final String icon;
    
    Priority(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }
}