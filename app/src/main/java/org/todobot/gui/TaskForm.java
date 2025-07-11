package org.todobot.gui;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.todobot.parsers.DateTimeParser;
import org.todobot.service.ToDoBotService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskForm {
    
    private final BotResponse.FormType formType;
    private final ToDoBotService service;
    private final VBox chatArea;
    private final Runnable scrollHandler;
    private final Consumer<String> messageHandler;
    
    public TaskForm(BotResponse.FormType formType, ToDoBotService service, VBox chatArea, 
                   Runnable scrollHandler, Consumer<String> messageHandler) {
        this.formType = formType;
        this.service = service;
        this.chatArea = chatArea;
        this.scrollHandler = scrollHandler;
        this.messageHandler = messageHandler;
    }
    
    public void show() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(15));
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.getStyleClass().add("form-container");
        
        // Add form-specific styling
        switch (formType) {
            case DEADLINE -> formBox.getStyleClass().add("deadline-form");
            case EVENT -> formBox.getStyleClass().add("event-form");
            default -> throw new IllegalArgumentException("Unexpected value: " + formType);
        }
        
        // Form title
        Label titleLabel = new Label(getFormTitle());
        titleLabel.getStyleClass().add("form-title");
        formBox.getChildren().add(titleLabel);
        
        // Description field (common to all forms)
        Label descLabel = new Label(getDescriptionLabel());
        descLabel.getStyleClass().add("field-label");
        TextField descField = new TextField();
        descField.setPromptText(getDescriptionPlaceholder());
        descField.setPrefWidth(300);
        formBox.getChildren().addAll(descLabel, descField);
        
        // Form-specific fields
        DatePicker datePicker = null;
        TimePickerComponent timePicker = null;
        DatePicker fromDatePicker = null, toDatePicker = null;
        TimePickerComponent fromTimePicker = null, toTimePicker = null;
        
        switch (formType) {
            case DEADLINE -> {
                // Date field
                Label dateLabel = new Label("Deadline Date:");
                dateLabel.getStyleClass().add("field-label");
                datePicker = new DatePicker();
                datePicker.setValue(LocalDate.now());
                
                // Time field
                Label timeLabel = new Label("Deadline Time:");
                timeLabel.getStyleClass().add("field-label");
                timePicker = new TimePickerComponent("12", "00");
                HBox timeBox = timePicker.createTimeBox();
                
                formBox.getChildren().addAll(dateLabel, datePicker, timeLabel, timeBox);
            }
            case EVENT -> {
                // From date/time
                Label fromLabel = new Label("From Date & Time:");
                fromLabel.getStyleClass().add("field-label");
                fromDatePicker = new DatePicker();
                fromDatePicker.setValue(LocalDate.now());
                
                fromTimePicker = new TimePickerComponent("09", "00");
                HBox fromTimeBox = new HBox(10);
                fromTimeBox.setAlignment(Pos.CENTER_LEFT);
                fromTimeBox.getChildren().add(fromDatePicker);
                fromTimeBox.getChildren().addAll(fromTimePicker.createTimeBox().getChildren());
                
                // To date/time
                Label toLabel = new Label("To Date & Time:");
                toLabel.getStyleClass().add("field-label");
                toDatePicker = new DatePicker();
                toDatePicker.setValue(LocalDate.now());
                
                toTimePicker = new TimePickerComponent("17", "00");
                HBox toTimeBox = new HBox(10);
                toTimeBox.setAlignment(Pos.CENTER_LEFT);
                toTimeBox.getChildren().add(toDatePicker);
                toTimeBox.getChildren().addAll(toTimePicker.createTimeBox().getChildren());
                
                formBox.getChildren().addAll(fromLabel, fromTimeBox, toLabel, toTimeBox);
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + formType);
        }
        
        // Buttons
        Button submitButton = new Button(getSubmitButtonText());
        Button cancelButton = new Button("Cancel");
        
        submitButton.getStyleClass().add(getSubmitButtonStyle());
        cancelButton.getStyleClass().add("cancel-button");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(submitButton, cancelButton);
        formBox.getChildren().add(buttonBox);
        
        // Event handlers
        final DatePicker finalDatePicker = datePicker;
        final TimePickerComponent finalTimePicker = timePicker;
        final DatePicker finalFromDatePicker = fromDatePicker;
        final TimePickerComponent finalFromTimePicker = fromTimePicker;
        final DatePicker finalToDatePicker = toDatePicker;
        final TimePickerComponent finalToTimePicker = toTimePicker;
        
        submitButton.setOnAction(e -> {
            String description = descField.getText().trim();
            
            if (description.isEmpty()) {
                messageHandler.accept("Please enter a description.");
                return;
            }
            
            String response = switch (formType) {
                case TODO -> {
                    messageHandler.accept("Created todo: " + description);
                    yield service.handleTodoTask(description);
                }
                case FIND -> {
                    messageHandler.accept("Searching for: " + description);
                    yield service.handleFindTask(description);
                }
                case DEADLINE -> {
                    if (finalDatePicker.getValue() == null) {
                        messageHandler.accept("Please select a date.");
                        yield null;
                    } else {
                        String dateTimeStr = DateTimeParser.formatForCommandInput(
                            finalDatePicker.getValue(), 
                            finalTimePicker.getHour(), 
                            finalTimePicker.getMinute()
                        );
                        messageHandler.accept("Created deadline: " + description + " by " + dateTimeStr);
                        yield service.handleDeadlineTask(description, finalDatePicker.getValue(), 
                            finalTimePicker.getHour(), finalTimePicker.getMinute());
                    }
                }
                case EVENT -> {
                    if (finalFromDatePicker.getValue() == null || finalToDatePicker.getValue() == null) {
                        messageHandler.accept("Please select both dates.");
                        yield null;
                    } else {
                        String fromDateTimeStr = DateTimeParser.formatForCommandInput(
                            finalFromDatePicker.getValue(), 
                            finalFromTimePicker.getHour(), 
                            finalFromTimePicker.getMinute()
                        );
                        String toDateTimeStr = DateTimeParser.formatForCommandInput(
                            finalToDatePicker.getValue(), 
                            finalToTimePicker.getHour(), 
                            finalToTimePicker.getMinute()
                        );
                        messageHandler.accept("Created event: " + description + " from " + fromDateTimeStr + " to " + toDateTimeStr);
                        yield service.handleEventTask(description, 
                            finalFromDatePicker.getValue(), finalFromTimePicker.getHour(), finalFromTimePicker.getMinute(),
                            finalToDatePicker.getValue(), finalToTimePicker.getHour(), finalToTimePicker.getMinute());
                    }
                }
            };
            
            if (response != null) {
                messageHandler.accept(response);
                chatArea.getChildren().remove(formBox);
                
                // Return to main menu
                BotResponse mainMenuResponse = service.processButtonClick("");
                addBotResponse(mainMenuResponse);
                
                scrollHandler.run();
            }
        });
        
        cancelButton.setOnAction(e -> {
            chatArea.getChildren().remove(formBox);
            BotResponse mainMenuResponse = service.processButtonClick("");
            addBotResponse(mainMenuResponse);
            scrollHandler.run();
        });
        
        // Enter key submits
        descField.setOnAction(e -> submitButton.fire());
        
        // Add form to chat area
        chatArea.getChildren().add(formBox);
        descField.requestFocus();
        scrollHandler.run();
    }
    
    private String getFormTitle() {
        return switch (formType) {
            case TODO -> "Create Todo Task";
            case FIND -> "Find Tasks";
            case DEADLINE -> "Create Deadline Task";
            case EVENT -> "Create Event";
        };
    }
    
    private String getDescriptionLabel() {
        return switch (formType) {
            case TODO -> "Task Description:";
            case FIND -> "Search Term:";
            case DEADLINE -> "Task Description:";
            case EVENT -> "Event Description:";
        };
    }
    
    private String getDescriptionPlaceholder() {
        return switch (formType) {
            case TODO -> "Enter your todo task...";
            case FIND -> "Enter search term...";
            case DEADLINE -> "Enter task description...";
            case EVENT -> "Enter event description...";
        };
    }
    
    private String getSubmitButtonText() {
        return switch (formType) {
            case TODO -> "Create Todo";
            case FIND -> "Find Tasks";
            case DEADLINE -> "Create Deadline Task";
            case EVENT -> "Create Event";
        };
    }
    
    private String getSubmitButtonStyle() {
        return switch (formType) {
            case TODO, DEADLINE -> "primary-button";
            case FIND -> "primary-button";
            case EVENT -> "warning-button";
        };
    }
    
    private void addBotResponse(BotResponse response) {
        // Add message
        messageHandler.accept(response.getMessage());
        
        // Add buttons or forms based on response type
        switch (response.getType()) {
            case BUTTONS -> {
                addButtonsToChat(response.getButtons());
            }
            case DROPDOWN -> {
                // Create dropdown form
                SimpleDropdownForm dropdownForm = new SimpleDropdownForm(service, chatArea, messageHandler, scrollHandler);
                dropdownForm.show(response.getTaskCount(), response.getBackButton());
            }
            case FORM -> {
                // Create another form
                TaskForm newForm = new TaskForm(response.getFormType(), service, chatArea, scrollHandler, messageHandler);
                newForm.show();
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + response.getType());
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