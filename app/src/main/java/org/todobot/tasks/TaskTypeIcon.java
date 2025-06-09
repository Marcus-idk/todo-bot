package org.todobot.tasks;

public enum TaskTypeIcon {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");
    
    private final String icon;
    
    TaskTypeIcon(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }
}