package org.todobot.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FindForm {
    
    private final FormManager.ServiceProvider serviceProvider;
    private final FormManager.ChatAreaProvider chatAreaProvider;
    private final FormManager.MessageHandler messageHandler;
    private final FormManager.ScrollHandler scrollHandler;
    private final FormManager.FocusHandler focusHandler;
    
    public FindForm(FormManager.ServiceProvider serviceProvider,
                   FormManager.ChatAreaProvider chatAreaProvider,
                   FormManager.MessageHandler messageHandler,
                   FormManager.ScrollHandler scrollHandler,
                   FormManager.FocusHandler focusHandler) {
        this.serviceProvider = serviceProvider;
        this.chatAreaProvider = chatAreaProvider;
        this.messageHandler = messageHandler;
        this.scrollHandler = scrollHandler;
        this.focusHandler = focusHandler;
    }
    
    public void show() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(15));
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.getStyleClass().add("form-container");
        
        // Form title
        Label titleLabel = new Label("Find Tasks");
        titleLabel.getStyleClass().add("form-title");
        
        // Search term
        Label searchLabel = new Label("Search Term:");
        searchLabel.getStyleClass().add("field-label");
        TextField searchField = new TextField();
        searchField.setPromptText("Enter search term...");
        searchField.setPrefWidth(300);
        
        // Buttons
        Button submitButton = new Button("Find Tasks");
        Button cancelButton = new Button("Cancel");
        
        submitButton.getStyleClass().add("primary-button");
        cancelButton.getStyleClass().add("cancel-button");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(submitButton, cancelButton);
        
        formBox.getChildren().addAll(titleLabel, searchLabel, searchField, buttonBox);
        
        // Event handlers
        submitButton.setOnAction(e -> {
            String searchTerm = searchField.getText().trim();
            
            if (!searchTerm.isEmpty()) {
                // Add user message
                messageHandler.addUserMessage("Searching for: " + searchTerm);
                
                // Pass raw data to service - let it handle command building
                String response = serviceProvider.handleFindTask(searchTerm);
                messageHandler.addBotMessage(response);
                
                // Remove form
                chatAreaProvider.removeChild(formBox);
                
                // Return to main menu
                String mainMenuResponse = serviceProvider.processButtonClick("");
                messageHandler.addBotResponse(mainMenuResponse);
                
                scrollHandler.scrollToBottom();
            } else {
                messageHandler.addBotMessage("Please enter a search term.");
            }
        });
        
        cancelButton.setOnAction(e -> {
            chatAreaProvider.removeChild(formBox);
            String mainMenuResponse = serviceProvider.processButtonClick("");
            messageHandler.addBotResponse(mainMenuResponse);
            scrollHandler.scrollToBottom();
        });
        
        // Enter key submits
        searchField.setOnAction(e -> submitButton.fire());
        
        // Add form to chat area
        chatAreaProvider.addChild(formBox);
        focusHandler.requestFocus(searchField);
        scrollHandler.scrollToBottom();
    }
}