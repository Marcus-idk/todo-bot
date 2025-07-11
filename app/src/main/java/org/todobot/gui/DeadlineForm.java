package org.todobot.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.todobot.parsers.DateTimeParser;
import java.time.LocalDate;

public class DeadlineForm {
    
    private final FormManager.ServiceProvider serviceProvider;
    private final FormManager.ChatAreaProvider chatAreaProvider;
    private final FormManager.MessageHandler messageHandler;
    private final FormManager.ScrollHandler scrollHandler;
    private final FormManager.FocusHandler focusHandler;
    
    public DeadlineForm(FormManager.ServiceProvider serviceProvider,
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
        formBox.getStyleClass().addAll("form-container", "deadline-form");
        
        // Form title
        Label titleLabel = new Label("Create Deadline Task");
        titleLabel.getStyleClass().add("form-title");
        
        // Task description
        Label descLabel = new Label("Task Description:");
        descLabel.getStyleClass().add("field-label");
        TextField descField = new TextField();
        descField.setPromptText("Enter task description...");
        descField.setPrefWidth(300);
        
        // Date selection
        Label dateLabel = new Label("Deadline Date:");
        dateLabel.getStyleClass().add("field-label");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        datePicker.setValue(LocalDate.now());
        
        // Time selection
        Label timeLabel = new Label("Deadline Time:");
        timeLabel.getStyleClass().add("field-label");
        
        TimePickerComponent timePicker = new TimePickerComponent("12", "00");
        HBox timeBox = timePicker.createTimeBox();
        
        // Buttons
        Button submitButton = new Button("Create Deadline Task");
        Button cancelButton = new Button("Cancel");
        
        submitButton.getStyleClass().add("primary-button");
        cancelButton.getStyleClass().add("cancel-button");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(submitButton, cancelButton);
        
        formBox.getChildren().addAll(titleLabel, descLabel, descField, dateLabel, datePicker, timeLabel, timeBox, buttonBox);
        
        // Event handlers
        submitButton.setOnAction(e -> {
            String description = descField.getText().trim();
            LocalDate selectedDate = datePicker.getValue();
            String selectedHour = timePicker.getHour();
            String selectedMinute = timePicker.getMinute();
            
            if (!description.isEmpty() && selectedDate != null && selectedHour != null && selectedMinute != null) {
                // Add user message
                String dateTimeStr = DateTimeParser.formatForCommandInput(selectedDate, selectedHour, selectedMinute);
                messageHandler.addUserMessage("Created deadline: " + description + " by " + dateTimeStr);
                
                // Pass raw data to service - let it handle command building
                String response = serviceProvider.handleDeadlineTask(description, selectedDate, selectedHour, selectedMinute);
                messageHandler.addBotMessage(response);
                
                // Remove form
                chatAreaProvider.removeChild(formBox);
                
                // Return to main menu
                String mainMenuResponse = serviceProvider.processButtonClick("");
                messageHandler.addBotResponse(mainMenuResponse);
                
                scrollHandler.scrollToBottom();
            } else {
                messageHandler.addBotMessage("Please fill in all fields.");
            }
        });
        
        cancelButton.setOnAction(e -> {
            chatAreaProvider.removeChild(formBox);
            String mainMenuResponse = serviceProvider.processButtonClick("");
            messageHandler.addBotResponse(mainMenuResponse);
            scrollHandler.scrollToBottom();
        });
        
        // Add form to chat area
        chatAreaProvider.addChild(formBox);
        focusHandler.requestFocus(descField);
        scrollHandler.scrollToBottom();
    }
}