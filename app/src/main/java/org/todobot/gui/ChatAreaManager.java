package org.todobot.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatAreaManager {
    
    public interface ButtonClickHandler {
        void handleButtonClick(String action);
    }
    
    public interface ButtonLabelProvider {
        String getButtonLabel(String action);
    }
    
    private final VBox chatArea;
    private final ButtonClickHandler buttonClickHandler;
    private final ButtonLabelProvider buttonLabelProvider;
    private final FormManager formManager;
    
    public ChatAreaManager(VBox chatArea, ButtonClickHandler buttonClickHandler, 
                          ButtonLabelProvider buttonLabelProvider, FormManager formManager) {
        this.chatArea = chatArea;
        this.buttonClickHandler = buttonClickHandler;
        this.buttonLabelProvider = buttonLabelProvider;
        this.formManager = formManager;
    }
    
    public void addUserMessage(String message) {
        Label userLabel = new Label("You: " + message);
        userLabel.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 10; -fx-background-radius: 10; -fx-text-fill: #1976d2;");
        userLabel.setMaxWidth(Double.MAX_VALUE);
        userLabel.setAlignment(Pos.CENTER_RIGHT);
        
        HBox userBox = new HBox();
        userBox.setAlignment(Pos.CENTER_RIGHT);
        userBox.getChildren().add(userLabel);
        
        chatArea.getChildren().add(userBox);
    }
    
    public void addBotMessage(String message) {
        Label botLabel = new Label("Bot: " + message);
        botLabel.setStyle("-fx-background-color: #f3e5f5; -fx-padding: 10; -fx-background-radius: 10; -fx-text-fill: #7b1fa2;");
        botLabel.setMaxWidth(Double.MAX_VALUE);
        botLabel.setAlignment(Pos.CENTER_LEFT);
        
        HBox botBox = new HBox();
        botBox.setAlignment(Pos.CENTER_LEFT);
        botBox.getChildren().add(botLabel);
        
        chatArea.getChildren().add(botBox);
    }
    
    public void addBotResponse(String response) {
        if (response.contains("|")) {
            String[] parts = response.split("\\|");
            String message = parts[0];
            
            // Add the message
            addBotMessage(message);
            
            // Check if this is a dropdown format
            if (parts.length >= 3 && parts[1].equals("DROPDOWN")) {
                int taskCount = Integer.parseInt(parts[2]);
                String backButton = parts.length > 3 ? parts[3] : "back";
                formManager.showDropdown(taskCount, backButton);
            } else if (parts.length >= 2 && parts[1].equals("DEADLINE_FORM")) {
                formManager.showDeadlineForm();
            } else if (parts.length >= 2 && parts[1].equals("EVENT_FORM")) {
                formManager.showEventForm();
            } else if (parts.length >= 2) {
                // Regular button format
                String buttonsStr = parts[1];
                if (!buttonsStr.isEmpty()) {
                    String[] buttonActions = buttonsStr.split(",");
                    addButtonsToChat(buttonActions);
                }
            }
        } else {
            // No buttons, just regular message
            addBotMessage(response);
        }
    }
    
    private void addButtonsToChat(String[] buttonActions) {
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        for (String action : buttonActions) {
            Button button = new Button(buttonLabelProvider.getButtonLabel(action));
            button.setStyle("-fx-background-color: #e1f5fe; -fx-text-fill: #0277bd; -fx-border-color: #0277bd; -fx-border-radius: 5; -fx-background-radius: 5;");
            button.setOnAction(e -> buttonClickHandler.handleButtonClick(action));
            buttonBox.getChildren().add(button);
        }
        
        chatArea.getChildren().add(buttonBox);
    }
}