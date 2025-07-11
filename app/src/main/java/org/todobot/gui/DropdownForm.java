package org.todobot.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DropdownForm {
    
    private final FormManager.ServiceProvider serviceProvider;
    private final FormManager.ChatAreaProvider chatAreaProvider;
    private final FormManager.MessageHandler messageHandler;
    private final FormManager.ScrollHandler scrollHandler;
    
    public DropdownForm(FormManager.ServiceProvider serviceProvider,
                       FormManager.ChatAreaProvider chatAreaProvider,
                       FormManager.MessageHandler messageHandler,
                       FormManager.ScrollHandler scrollHandler) {
        this.serviceProvider = serviceProvider;
        this.chatAreaProvider = chatAreaProvider;
        this.messageHandler = messageHandler;
        this.scrollHandler = scrollHandler;
    }
    
    public void show(int taskCount, String backButton) {
        VBox dropdownBox = new VBox(10);
        dropdownBox.setPadding(new Insets(10));
        dropdownBox.setAlignment(Pos.CENTER_LEFT);
        
        // Create task selection dropdown
        ComboBox<String> taskComboBox = new ComboBox<>();
        taskComboBox.setPromptText("Select task...");
        for (int i = 1; i <= taskCount; i++) {
            taskComboBox.getItems().add("Task " + i);
        }
        
        // Create action selection dropdown
        ComboBox<String> actionComboBox = new ComboBox<>();
        actionComboBox.setPromptText("Select action...");
        actionComboBox.getItems().addAll("Mark", "Unmark", "Delete");
        
        // Create buttons
        Button goButton = new Button("Go");
        Button backButtonUI = new Button("Back");
        
        // Style the dropdowns and buttons
        taskComboBox.setPrefWidth(150);
        actionComboBox.setPrefWidth(150);
        goButton.getStyleClass().add("secondary-button");
        backButtonUI.getStyleClass().add("delete-button");
        
        // Create horizontal layout for dropdowns and buttons
        HBox controlsBox = new HBox(10);
        controlsBox.setAlignment(Pos.CENTER_LEFT);
        controlsBox.getChildren().addAll(taskComboBox, actionComboBox, goButton, backButtonUI);
        
        // Add label
        Label instructionLabel = new Label("Select a task and action:");
        instructionLabel.getStyleClass().add("instruction-label");
        
        dropdownBox.getChildren().addAll(instructionLabel, controlsBox);
        
        // Event handlers
        goButton.setOnAction(e -> {
            String selectedTask = taskComboBox.getSelectionModel().getSelectedItem();
            String selectedAction = actionComboBox.getSelectionModel().getSelectedItem();
            
            if (selectedTask != null && selectedAction != null) {
                handleDropdownAction(selectedTask, selectedAction);
                // Remove dropdown after action
                chatAreaProvider.removeChild(dropdownBox);
            }
        });
        
        backButtonUI.setOnAction(e -> {
            // Remove dropdown and go back
            chatAreaProvider.removeChild(dropdownBox);
            String mainMenuResponse = serviceProvider.processButtonClick(backButton);
            messageHandler.addBotResponse(mainMenuResponse);
        });
        
        // Add dropdown to chat area
        chatAreaProvider.addChild(dropdownBox);
        scrollHandler.scrollToBottom();
    }
    
    private void handleDropdownAction(String selectedTask, String selectedAction) {
        // Add user message showing what they selected
        messageHandler.addUserMessage("Selected: " + selectedTask + " - " + selectedAction);
        
        // Process through service - business logic handled there
        String response = serviceProvider.handleDropdownSelection(selectedTask, selectedAction);
        messageHandler.addBotMessage(response);
        
        // Return to main menu
        String mainMenuResponse = serviceProvider.processButtonClick("");
        messageHandler.addBotResponse(mainMenuResponse);
        
        scrollHandler.scrollToBottom();
    }
}