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
    
    public static Priority fromString(String token) {
        if (token == null) {
            return MEDIUM;
        }
        
        return switch (token.toLowerCase()) {
            case "high", "h" -> HIGH;
            case "low", "l" -> LOW;
            default -> MEDIUM;
        };
    }
}