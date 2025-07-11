package org.todobot.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.todobot.service.ToDoBotService;

import java.util.function.Consumer;

public class SimpleChatAreaManager {
    
    private final VBox chatArea;
    private final ToDoBotService service;
    private final SimpleFormManager formManager;
    private final Runnable scrollHandler;
    private final Consumer<String> exitHandler;
    
    public SimpleChatAreaManager(VBox chatArea, ToDoBotService service, 
                               SimpleFormManager formManager, Runnable scrollHandler,
                               Consumer<String> exitHandler) {
        this.chatArea = chatArea;
        this.service = service;
        this.formManager = formManager;
        this.scrollHandler = scrollHandler;
        this.exitHandler = exitHandler;
    }
    
    public void addUserMessage(String message) {
        Label userLabel = new Label("You: " + message);
        userLabel.getStyleClass().add("user-message");
        userLabel.setMaxWidth(Double.MAX_VALUE);
        userLabel.setAlignment(Pos.CENTER_RIGHT);
        
        HBox userBox = new HBox();
        userBox.setAlignment(Pos.CENTER_RIGHT);
        userBox.getChildren().add(userLabel);
        
        chatArea.getChildren().add(userBox);
        scrollHandler.run();
    }
    
    public void addBotMessage(String message) {
        Label botLabel = new Label("Bot: " + message);
        botLabel.getStyleClass().add("bot-message");
        botLabel.setMaxWidth(Double.MAX_VALUE);
        botLabel.setAlignment(Pos.CENTER_LEFT);
        
        HBox botBox = new HBox();
        botBox.setAlignment(Pos.CENTER_LEFT);
        botBox.getChildren().add(botLabel);
        
        chatArea.getChildren().add(botBox);
        scrollHandler.run();
    }
    
    public void addBotResponse(BotResponse response) {
        addBotMessage(response.getMessage());
        
        switch (response.getType()) {
            case BUTTONS -> {
                addButtonsToChat(response.getButtons());
            }
            case DROPDOWN -> {
                formManager.showDropdown(response.getTaskCount(), response.getBackButton());
            }
            case FORM -> {
                formManager.showForm(response.getFormType());
            }
        }
    }
    
    private void addButtonsToChat(java.util.List<String> buttonActions) {
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        for (String action : buttonActions) {
            Button button = new Button(getButtonLabel(action));
            button.getStyleClass().add("action-button");
            button.setOnAction(e -> handleButtonClick(action));
            buttonBox.getChildren().add(button);
        }
        
        chatArea.getChildren().add(buttonBox);
        scrollHandler.run();
    }
    
    private void handleButtonClick(String action) {
        addUserMessage("Clicked: " + getButtonLabel(action));
        
        if (action.equals("exit")) {
            addBotMessage("Saving & Closing... Bye. Hope to see you again soon!");
            exitHandler.accept("exit");
            return;
        }
        
        BotResponse response = service.processButtonClick(action);
        addBotResponse(response);
    }
    
    private String getButtonLabel(String action) {
        return switch (action) {
            case "add_task" -> "Add Task";
            case "view_tasks" -> "View Tasks";
            case "find_tasks" -> "Find Tasks";
            case "help" -> "Help";
            case "exit" -> "Exit";
            case "todo" -> "Todo";
            case "deadline" -> "Deadline";
            case "event" -> "Event";
            case "back" -> "Back";
            default -> {
                if (action.startsWith("mark_")) {
                    yield "Mark " + action.substring(5);
                } else if (action.startsWith("delete_")) {
                    yield "Delete " + action.substring(7);
                }
                yield action;
            }
        };
    }
}