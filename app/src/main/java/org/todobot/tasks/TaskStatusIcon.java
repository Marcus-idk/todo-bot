package org.todobot.tasks;

public enum TaskStatusIcon {
    DONE("X"),
    NOT_DONE(" ");
    
    private final String icon;
    
    TaskStatusIcon(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }
}