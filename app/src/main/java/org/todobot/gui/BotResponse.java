package org.todobot.gui;

import java.util.List;

public class BotResponse {
    
    public enum ResponseType {
        MESSAGE,
        BUTTONS,
        DROPDOWN,
        FORM
    }
    
    public enum FormType {
        TODO,
        FIND,
        DEADLINE,
        EVENT
    }
    
    private final String message;
    private final ResponseType type;
    private final List<String> buttons;
    private final FormType formType;
    private final int taskCount;
    private final String backButton;
    
    // Simple message
    public BotResponse(String message) {
        this.message = message;
        this.type = ResponseType.MESSAGE;
        this.buttons = null;
        this.formType = null;
        this.taskCount = 0;
        this.backButton = null;
    }
    
    // Message with buttons
    public BotResponse(String message, List<String> buttons) {
        this.message = message;
        this.type = ResponseType.BUTTONS;
        this.buttons = buttons;
        this.formType = null;
        this.taskCount = 0;
        this.backButton = null;
    }
    
    // Message with dropdown
    public BotResponse(String message, int taskCount, String backButton) {
        this.message = message;
        this.type = ResponseType.DROPDOWN;
        this.buttons = null;
        this.formType = null;
        this.taskCount = taskCount;
        this.backButton = backButton;
    }
    
    // Message with form
    public BotResponse(String message, FormType formType) {
        this.message = message;
        this.type = ResponseType.FORM;
        this.buttons = null;
        this.formType = formType;
        this.taskCount = 0;
        this.backButton = null;
    }
    
    // Getters
    public String getMessage() { return message; }
    public ResponseType getType() { return type; }
    public List<String> getButtons() { return buttons; }
    public FormType getFormType() { return formType; }
    public int getTaskCount() { return taskCount; }
    public String getBackButton() { return backButton; }
}