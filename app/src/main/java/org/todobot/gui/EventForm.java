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

public class EventForm {
    
    private final FormManager.ServiceProvider serviceProvider;
    private final FormManager.ChatAreaProvider chatAreaProvider;
    private final FormManager.MessageHandler messageHandler;
    private final FormManager.ScrollHandler scrollHandler;
    private final FormManager.FocusHandler focusHandler;
    
    public EventForm(FormManager.ServiceProvider serviceProvider,
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
        formBox.getStyleClass().addAll("form-container", "event-form");
        
        // Form title
        Label titleLabel = new Label("Create Event");
        titleLabel.getStyleClass().add("form-title");
        
        // Event description
        Label descLabel = new Label("Event Description:");
        descLabel.getStyleClass().add("field-label");
        TextField descField = new TextField();
        descField.setPromptText("Enter event description...");
        descField.setPrefWidth(300);
        
        // From date/time
        Label fromLabel = new Label("From Date & Time:");
        fromLabel.getStyleClass().add("field-label");
        DatePicker fromDatePicker = new DatePicker();
        fromDatePicker.setValue(LocalDate.now());
        
        TimePickerComponent fromTimePicker = new TimePickerComponent("09", "00");
        HBox fromTimeBox = new HBox(10);
        fromTimeBox.setAlignment(Pos.CENTER_LEFT);
        fromTimeBox.getChildren().add(fromDatePicker);
        fromTimeBox.getChildren().addAll(fromTimePicker.createTimeBox().getChildren());
        
        // To date/time
        Label toLabel = new Label("To Date & Time:");
        toLabel.getStyleClass().add("field-label");
        DatePicker toDatePicker = new DatePicker();
        toDatePicker.setValue(LocalDate.now());
        
        TimePickerComponent toTimePicker = new TimePickerComponent("17", "00");
        HBox toTimeBox = new HBox(10);
        toTimeBox.setAlignment(Pos.CENTER_LEFT);
        toTimeBox.getChildren().add(toDatePicker);
        toTimeBox.getChildren().addAll(toTimePicker.createTimeBox().getChildren());
        
        // Buttons
        Button submitButton = new Button("Create Event");
        Button cancelButton = new Button("Cancel");
        
        submitButton.getStyleClass().add("warning-button");
        cancelButton.getStyleClass().add("cancel-button");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(submitButton, cancelButton);
        
        formBox.getChildren().addAll(titleLabel, descLabel, descField, fromLabel, fromTimeBox, toLabel, toTimeBox, buttonBox);
        
        // Event handlers
        submitButton.setOnAction(e -> {
            String description = descField.getText().trim();
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            String fromHour = fromTimePicker.getHour();
            String fromMinute = fromTimePicker.getMinute();
            String toHour = toTimePicker.getHour();
            String toMinute = toTimePicker.getMinute();
            
            if (!description.isEmpty() && fromDate != null && toDate != null && 
                fromHour != null && fromMinute != null && toHour != null && toMinute != null) {
                
                // Add user message
                String fromDateTimeStr = DateTimeParser.formatForCommandInput(fromDate, fromHour, fromMinute);
                String toDateTimeStr = DateTimeParser.formatForCommandInput(toDate, toHour, toMinute);
                messageHandler.addUserMessage("Created event: " + description + " from " + fromDateTimeStr + " to " + toDateTimeStr);
                
                // Pass raw data to service - let it handle command building
                String response = serviceProvider.handleEventTask(description, fromDate, fromHour, fromMinute, toDate, toHour, toMinute);
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