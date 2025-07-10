package org.todobot.app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.todobot.service.ToDoBotService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ToDoBotGUI extends Application {
    private VBox chatArea;
    private ScrollPane scrollPane;
    private TextField inputField;
    private Button sendButton;
    private Button toggleButton;
    private HBox inputArea;
    private boolean textInputVisible = false; // Default is hidden
    private ToDoBotService service;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.service = new ToDoBotService();
        
        // Initial mode is button mode (textInputVisible = false)
        
        primaryStage.setTitle("TODO Bot");

        // Create main layout
        BorderPane root = new BorderPane();

        // Create chat area
        chatArea = new VBox(10);
        chatArea.setPadding(new Insets(10));
        chatArea.setAlignment(Pos.TOP_LEFT);
        
        // Add listener to auto-scroll when content height changes
        chatArea.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollToBottom();
        });

        // Create scroll pane for chat area
        scrollPane = new ScrollPane(chatArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Create input area
        inputArea = new HBox(10);
        inputArea.setPadding(new Insets(10));
        inputArea.setAlignment(Pos.CENTER);

        inputField = new TextField();
        inputField.setPromptText("Type a command (e.g., todo buy milk, list, help)...");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        sendButton = new Button("Send");
        sendButton.setMinWidth(80);

        toggleButton = new Button("▲");
        toggleButton.setMinWidth(40);
        toggleButton.setStyle("-fx-font-size: 14px;");

        inputArea.getChildren().addAll(inputField, sendButton);
        
        // Initially hide the input area
        inputArea.setVisible(textInputVisible);
        inputArea.setManaged(textInputVisible);

        // Create toggle button container that's always visible
        HBox toggleContainer = new HBox();
        toggleContainer.setAlignment(Pos.CENTER);
        toggleContainer.setPadding(new Insets(5));
        toggleContainer.getChildren().add(toggleButton);

        // Create bottom container to hold both toggle and input area
        VBox bottomContainer = new VBox();
        bottomContainer.getChildren().addAll(toggleContainer, inputArea);

        // Set up the layout
        root.setCenter(scrollPane);
        root.setBottom(bottomContainer);

        // Add initial welcome message
        addBotMessage("Hello! I'm your TODO Bot. What can I do for you?");
        if (textInputVisible) {
            addBotMessage("Type commands like: todo buy milk, list, help, bye");
        } else {
            // Show main menu buttons
            String mainMenuResponse = service.processButtonClick("");
            addBotResponse(mainMenuResponse);
        }
        
        // Ensure initial messages are visible
        scrollToBottom();

        // Set up event handlers
        setupEventHandlers();

        // Create scene and show stage
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        
        // Handle window close event
        primaryStage.setOnCloseRequest(e -> {
            service.cleanup();
        });
        
        primaryStage.show();

        // Focus on input field
        inputField.requestFocus();
    }

    private void setupEventHandlers() {
        // Send button click
        sendButton.setOnAction(e -> handleSendMessage());

        // Enter key press in input field
        inputField.setOnAction(e -> handleSendMessage());

        // Toggle button click
        toggleButton.setOnAction(e -> handleToggleInput());
    }

    private void handleToggleInput() {
        textInputVisible = !textInputVisible;
        inputArea.setVisible(textInputVisible);
        inputArea.setManaged(textInputVisible);
        
        // Update button text
        toggleButton.setText(textInputVisible ? "▼" : "▲");
        
        // Add appropriate message based on mode
        if (textInputVisible) {
            addBotMessage("Text input mode enabled. Type commands like: todo buy milk, list, help, bye");
            inputField.requestFocus();
        } else {
            addBotMessage("Button mode enabled. Use the buttons below to interact.");
            String mainMenuResponse = service.processButtonClick("");
            addBotResponse(mainMenuResponse);
        }
        
        scrollToBottom();
    }

    private void handleSendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // Add user message
            addUserMessage(message);
            
            // Clear input field
            inputField.clear();
            
            // Process command through service
            String response = service.processCommand(message);
            
            // Check if bye command after processing
            if (service.shouldExit()) {
                addBotMessage("Saving & Closing... Bye. Hope to see you again soon!");
                
                // Close after delay to show message using Timeline (non-blocking)
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> {
                    service.cleanup();
                    primaryStage.close();
                }));
                timeline.play();
            } else {
                // If in button mode, check if we should show buttons after response
                if (!textInputVisible) {
                    addBotMessage(response);
                    // Return to main menu after processing command
                    String mainMenuResponse = service.processButtonClick("");
                    addBotResponse(mainMenuResponse);
                } else {
                    addBotMessage(response);
                }
            }
            
            // Scroll to bottom
            scrollToBottom();
        }
    }

    private void addUserMessage(String message) {
        Label userLabel = new Label("You: " + message);
        userLabel.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 10; -fx-background-radius: 10; -fx-text-fill: #1976d2;");
        userLabel.setMaxWidth(Double.MAX_VALUE);
        userLabel.setAlignment(Pos.CENTER_RIGHT);
        
        HBox userBox = new HBox();
        userBox.setAlignment(Pos.CENTER_RIGHT);
        userBox.getChildren().add(userLabel);
        
        chatArea.getChildren().add(userBox);
    }

    private void addBotMessage(String message) {
        Label botLabel = new Label("Bot: " + message);
        botLabel.setStyle("-fx-background-color: #f3e5f5; -fx-padding: 10; -fx-background-radius: 10; -fx-text-fill: #7b1fa2;");
        botLabel.setMaxWidth(Double.MAX_VALUE);
        botLabel.setAlignment(Pos.CENTER_LEFT);
        
        HBox botBox = new HBox();
        botBox.setAlignment(Pos.CENTER_LEFT);
        botBox.getChildren().add(botLabel);
        
        chatArea.getChildren().add(botBox);
    }
    
    private void addBotResponse(String response) {
        if (response.contains("|")) {
            String[] parts = response.split("\\|");
            String message = parts[0];
            
            // Add the message
            addBotMessage(message);
            
            // Check if this is a dropdown format
            if (parts.length >= 3 && parts[1].equals("DROPDOWN")) {
                int taskCount = Integer.parseInt(parts[2]);
                String backButton = parts.length > 3 ? parts[3] : "back";
                addDropdownToChat(taskCount, backButton);
            } else if (parts.length >= 2 && parts[1].equals("DEADLINE_FORM")) {
                addDeadlineFormToChat();
            } else if (parts.length >= 2 && parts[1].equals("EVENT_FORM")) {
                addEventFormToChat();
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
            Button button = new Button(getButtonLabel(action));
            button.setStyle("-fx-background-color: #e1f5fe; -fx-text-fill: #0277bd; -fx-border-color: #0277bd; -fx-border-radius: 5; -fx-background-radius: 5;");
            button.setOnAction(e -> handleButtonClick(action));
            buttonBox.getChildren().add(button);
        }
        
        chatArea.getChildren().add(buttonBox);
    }
    
    private void addDropdownToChat(int taskCount, String backButton) {
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
        goButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        backButtonUI.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        
        // Create horizontal layout for dropdowns and buttons
        HBox controlsBox = new HBox(10);
        controlsBox.setAlignment(Pos.CENTER_LEFT);
        controlsBox.getChildren().addAll(taskComboBox, actionComboBox, goButton, backButtonUI);
        
        // Add label
        Label instructionLabel = new Label("Select a task and action:");
        instructionLabel.setStyle("-fx-font-weight: bold;");
        
        dropdownBox.getChildren().addAll(instructionLabel, controlsBox);
        
        // Event handlers
        goButton.setOnAction(e -> {
            String selectedTask = taskComboBox.getSelectionModel().getSelectedItem();
            String selectedAction = actionComboBox.getSelectionModel().getSelectedItem();
            
            if (selectedTask != null && selectedAction != null) {
                handleDropdownAction(selectedTask, selectedAction);
                // Remove dropdown after action
                chatArea.getChildren().remove(dropdownBox);
            }
        });
        
        backButtonUI.setOnAction(e -> {
            // Remove dropdown and go back
            chatArea.getChildren().remove(dropdownBox);
            handleButtonClick(backButton);
        });
        
        chatArea.getChildren().add(dropdownBox);
        scrollToBottom();
    }
    
    private void handleDropdownAction(String selectedTask, String selectedAction) {
        // Add user message showing what they selected
        addUserMessage("Selected: " + selectedTask + " - " + selectedAction);
        
        // Process through service - business logic handled there
        String response = service.handleDropdownSelection(selectedTask, selectedAction);
        addBotMessage(response);
        
        // Return to main menu
        String mainMenuResponse = service.processButtonClick("");
        addBotResponse(mainMenuResponse);
        
        scrollToBottom();
    }
    
    private void addDeadlineFormToChat() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(15));
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setStyle("-fx-background-color: #f0f8ff; -fx-border-color: #4682b4; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        // Form title
        Label titleLabel = new Label("Create Deadline Task");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Task description
        Label descLabel = new Label("Task Description:");
        descLabel.setStyle("-fx-font-weight: bold;");
        TextField descField = new TextField();
        descField.setPromptText("Enter task description...");
        descField.setPrefWidth(300);
        
        // Date selection
        Label dateLabel = new Label("Deadline Date:");
        dateLabel.setStyle("-fx-font-weight: bold;");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        datePicker.setValue(LocalDate.now());
        
        // Time selection
        Label timeLabel = new Label("Deadline Time:");
        timeLabel.setStyle("-fx-font-weight: bold;");
        
        ComboBox<String> hourBox = new ComboBox<>();
        ComboBox<String> minuteBox = new ComboBox<>();
        
        // Populate hour dropdown (00-23)
        for (int i = 0; i < 24; i++) {
            hourBox.getItems().add(String.format("%02d", i));
        }
        hourBox.setValue("12");
        
        // Populate minute dropdown (00-59)
        for (int i = 0; i < 60; i++) {
            minuteBox.getItems().add(String.format("%02d", i));
        }
        minuteBox.setValue("00");
        
        HBox timeBox = new HBox(10);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        timeBox.getChildren().addAll(hourBox, new Label(":"), minuteBox);
        
        // Buttons
        Button submitButton = new Button("Create Deadline Task");
        Button cancelButton = new Button("Cancel");
        
        submitButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(submitButton, cancelButton);
        
        formBox.getChildren().addAll(titleLabel, descLabel, descField, dateLabel, datePicker, timeLabel, timeBox, buttonBox);
        
        // Event handlers
        submitButton.setOnAction(e -> {
            String description = descField.getText().trim();
            LocalDate selectedDate = datePicker.getValue();
            String selectedHour = hourBox.getValue();
            String selectedMinute = minuteBox.getValue();
            
            if (!description.isEmpty() && selectedDate != null && selectedHour != null && selectedMinute != null) {
                // Build deadline command
                String dateStr = selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String timeStr = selectedHour + ":" + selectedMinute;
                String command = description + " /by " + dateStr + " " + timeStr;
                String fullCommand = "deadline " + command;
                
                // Add user message
                addUserMessage("Created deadline: " + description + " by " + dateStr + " " + selectedHour + ":" + selectedMinute);
                
                // Process command
                String response = service.processCommand(fullCommand);
                addBotMessage(response);
                
                // Remove form
                chatArea.getChildren().remove(formBox);
                
                // Return to main menu
                String mainMenuResponse = service.processButtonClick("");
                addBotResponse(mainMenuResponse);
                
                scrollToBottom();
            } else {
                addBotMessage("Please fill in all fields.");
            }
        });
        
        cancelButton.setOnAction(e -> {
            chatArea.getChildren().remove(formBox);
            String mainMenuResponse = service.processButtonClick("");
            addBotResponse(mainMenuResponse);
            scrollToBottom();
        });
        
        chatArea.getChildren().add(formBox);
        descField.requestFocus();
        scrollToBottom();
    }
    
    private void addEventFormToChat() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(15));
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setStyle("-fx-background-color: #fff8dc; -fx-border-color: #daa520; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        // Form title
        Label titleLabel = new Label("Create Event");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Event description
        Label descLabel = new Label("Event Description:");
        descLabel.setStyle("-fx-font-weight: bold;");
        TextField descField = new TextField();
        descField.setPromptText("Enter event description...");
        descField.setPrefWidth(300);
        
        // From date/time
        Label fromLabel = new Label("From Date & Time:");
        fromLabel.setStyle("-fx-font-weight: bold;");
        DatePicker fromDatePicker = new DatePicker();
        fromDatePicker.setValue(LocalDate.now());
        
        ComboBox<String> fromHourBox = new ComboBox<>();
        ComboBox<String> fromMinuteBox = new ComboBox<>();
        
        // Populate from time dropdowns
        for (int i = 0; i < 24; i++) {
            fromHourBox.getItems().add(String.format("%02d", i));
        }
        fromHourBox.setValue("09");
        
        for (int i = 0; i < 60; i++) {
            fromMinuteBox.getItems().add(String.format("%02d", i));
        }
        fromMinuteBox.setValue("00");
        
        HBox fromTimeBox = new HBox(10);
        fromTimeBox.setAlignment(Pos.CENTER_LEFT);
        fromTimeBox.getChildren().addAll(fromDatePicker, fromHourBox, new Label(":"), fromMinuteBox);
        
        // To date/time
        Label toLabel = new Label("To Date & Time:");
        toLabel.setStyle("-fx-font-weight: bold;");
        DatePicker toDatePicker = new DatePicker();
        toDatePicker.setValue(LocalDate.now());
        
        ComboBox<String> toHourBox = new ComboBox<>();
        ComboBox<String> toMinuteBox = new ComboBox<>();
        
        // Populate to time dropdowns
        for (int i = 0; i < 24; i++) {
            toHourBox.getItems().add(String.format("%02d", i));
        }
        toHourBox.setValue("17");
        
        for (int i = 0; i < 60; i++) {
            toMinuteBox.getItems().add(String.format("%02d", i));
        }
        toMinuteBox.setValue("00");
        
        HBox toTimeBox = new HBox(10);
        toTimeBox.setAlignment(Pos.CENTER_LEFT);
        toTimeBox.getChildren().addAll(toDatePicker, toHourBox, new Label(":"), toMinuteBox);
        
        // Buttons
        Button submitButton = new Button("Create Event");
        Button cancelButton = new Button("Cancel");
        
        submitButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(submitButton, cancelButton);
        
        formBox.getChildren().addAll(titleLabel, descLabel, descField, fromLabel, fromTimeBox, toLabel, toTimeBox, buttonBox);
        
        // Event handlers
        submitButton.setOnAction(e -> {
            String description = descField.getText().trim();
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            String fromHour = fromHourBox.getValue();
            String fromMinute = fromMinuteBox.getValue();
            String toHour = toHourBox.getValue();
            String toMinute = toMinuteBox.getValue();
            
            if (!description.isEmpty() && fromDate != null && toDate != null && 
                fromHour != null && fromMinute != null && toHour != null && toMinute != null) {
                
                // Build event command
                String fromDateStr = fromDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String toDateStr = toDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String fromTimeStr = fromHour + ":" + fromMinute;
                String toTimeStr = toHour + ":" + toMinute;
                String command = description + " /from " + fromDateStr + " " + fromTimeStr + " /to " + toDateStr + " " + toTimeStr;
                String fullCommand = "event " + command;
                
                // Add user message
                addUserMessage("Created event: " + description + " from " + fromDateStr + " " + fromHour + ":" + fromMinute + 
                              " to " + toDateStr + " " + toHour + ":" + toMinute);
                
                // Process command
                String response = service.processCommand(fullCommand);
                addBotMessage(response);
                
                // Remove form
                chatArea.getChildren().remove(formBox);
                
                // Return to main menu
                String mainMenuResponse = service.processButtonClick("");
                addBotResponse(mainMenuResponse);
                
                scrollToBottom();
            } else {
                addBotMessage("Please fill in all fields.");
            }
        });
        
        cancelButton.setOnAction(e -> {
            chatArea.getChildren().remove(formBox);
            String mainMenuResponse = service.processButtonClick("");
            addBotResponse(mainMenuResponse);
            scrollToBottom();
        });
        
        chatArea.getChildren().add(formBox);
        descField.requestFocus();
        scrollToBottom();
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
                yield action; // fallback
            }
        };
    }
    
    private void handleButtonClick(String action) {
        // Add user message showing what they clicked
        addUserMessage("Clicked: " + getButtonLabel(action));
        
        // Handle special cases
        if (action.equals("exit")) {
            addBotMessage("Saving & Closing... Bye. Hope to see you again soon!");
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> {
                service.cleanup();
                primaryStage.close();
            }));
            timeline.play();
            return;
        }
        
        // Process button click through service
        String response = service.processButtonClick(action);
        
        // Check if this is a text input request
        if (action.equals("todo") || action.equals("find_tasks")) {
            addBotMessage(response);
            showTextInputForTask(action);
        } else {
            addBotResponse(response);
        }
        
        // Scroll to bottom
        scrollToBottom();
    }
    
    private void showTextInputForTask(String taskType) {
        HBox inputBox = new HBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER_LEFT);
        
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter your " + taskType + " task...");
        taskInput.setPrefWidth(300);
        
        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");
        
        submitButton.setOnAction(e -> {
            String taskText = taskInput.getText().trim();
            if (!taskText.isEmpty()) {
                // Add user message
                addUserMessage(taskText);
                
                // Process the task command
                String command = service.buildTaskCommand(taskType, taskText);
                
                String response = service.processCommand(command);
                addBotMessage(response);
                
                // Remove the input box
                chatArea.getChildren().remove(inputBox);
                
                // Return to main menu
                String mainMenuResponse = service.processButtonClick("");
                addBotResponse(mainMenuResponse);
                
                scrollToBottom();
            }
        });
        
        cancelButton.setOnAction(e -> {
            // Remove the input box
            chatArea.getChildren().remove(inputBox);
            
            // Return to main menu
            String mainMenuResponse = service.processButtonClick("");
            addBotResponse(mainMenuResponse);
            
            scrollToBottom();
        });
        
        taskInput.setOnAction(e -> submitButton.fire()); // Enter key submits
        
        inputBox.getChildren().addAll(taskInput, submitButton, cancelButton);
        chatArea.getChildren().add(inputBox);
        
        // Focus on the input field
        taskInput.requestFocus();
        scrollToBottom();
    }
    

    private void scrollToBottom() {
        // Scroll to bottom after a short delay to ensure layout is complete
        javafx.application.Platform.runLater(() -> {
            scrollPane.setVvalue(1.0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}