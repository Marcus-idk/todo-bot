package org.todobot.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.todobot.service.ToDoBotService;

import java.util.function.Consumer;

public class SimpleDropdownForm {
    
    private final ToDoBotService service;
    private final VBox chatArea;
    private final Consumer<String> messageHandler;
    private final Runnable scrollHandler;
    
    public SimpleDropdownForm(ToDoBotService service, VBox chatArea, 
                            Consumer<String> messageHandler, Runnable scrollHandler) {
        this.service = service;
        this.chatArea = chatArea;
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
                chatArea.getChildren().remove(dropdownBox);
            }
        });
        
        backButtonUI.setOnAction(e -> {
            chatArea.getChildren().remove(dropdownBox);
            BotResponse mainMenuResponse = service.processButtonClick(backButton);
            addBotResponse(mainMenuResponse);
        });
        
        // Add dropdown to chat area
        chatArea.getChildren().add(dropdownBox);
        scrollHandler.run();
    }
    
    private void handleDropdownAction(String selectedTask, String selectedAction) {
        messageHandler.accept("Selected: " + selectedTask + " - " + selectedAction);
        
        String response = service.handleDropdownSelection(selectedTask, selectedAction);
        messageHandler.accept(response);
        
        // Return to main menu
        BotResponse mainMenuResponse = service.processButtonClick("");
        addBotResponse(mainMenuResponse);
        
        scrollHandler.run();
    }
    
    private void addBotResponse(BotResponse response) {
        messageHandler.accept(response.getMessage());
        
        switch (response.getType()) {
            case BUTTONS -> {
                addButtonsToChat(response.getButtons());
            }
            case DROPDOWN -> {
                show(response.getTaskCount(), response.getBackButton());
            }
            case FORM -> {
                TaskForm form = new TaskForm(response.getFormType(), service, chatArea, scrollHandler, messageHandler);
                form.show();
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
            button.setOnAction(e -> {
                messageHandler.accept("Clicked: " + getButtonLabel(action));
                
                if (action.equals("exit")) {
                    messageHandler.accept("Saving & Closing... Bye. Hope to see you again soon!");
                    return;
                }
                
                BotResponse response = service.processButtonClick(action);
                addBotResponse(response);
            });
            buttonBox.getChildren().add(button);
        }
        
        chatArea.getChildren().add(buttonBox);
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