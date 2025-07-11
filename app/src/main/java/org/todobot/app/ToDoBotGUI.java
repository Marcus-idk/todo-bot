package org.todobot.app;

import java.time.LocalDate;

import org.todobot.gui.ChatAreaManager;
import org.todobot.gui.FormManager;
import org.todobot.service.ToDoBotService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private ChatAreaManager chatAreaManager;
    private FormManager formManager;

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
        toggleButton.getStyleClass().add("toggle-button");

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

        // Initialize FormManager
        formManager = new FormManager(
            // ServiceProvider interface
            new FormManager.ServiceProvider() {
                @Override
                public String handleDeadlineTask(String description, LocalDate date, String hour, String minute) {
                    return service.handleDeadlineTask(description, date, hour, minute);
                }
                
                @Override
                public String handleEventTask(String description, LocalDate fromDate, String fromHour, String fromMinute, LocalDate toDate, String toHour, String toMinute) {
                    return service.handleEventTask(description, fromDate, fromHour, fromMinute, toDate, toHour, toMinute);
                }
                
                @Override
                public String handleDropdownSelection(String selectedTask, String selectedAction) {
                    return service.handleDropdownSelection(selectedTask, selectedAction);
                }
                
                @Override
                public String processButtonClick(String action) {
                    return service.processButtonClick(action);
                }
            },
            // ChatAreaProvider interface
            new FormManager.ChatAreaProvider() {
                @Override
                public void addChild(VBox formBox) {
                    chatArea.getChildren().add(formBox);
                }
                
                @Override
                public void removeChild(VBox formBox) {
                    chatArea.getChildren().remove(formBox);
                }
            },
            // ScrollHandler interface
            this::scrollToBottom,
            // FocusHandler interface
            (field) -> field.requestFocus()
        );

        // Initialize ChatAreaManager
        chatAreaManager = new ChatAreaManager(
            chatArea,
            this::handleButtonClick,
            this::getButtonLabel,
            formManager
        );
        
        // Set the MessageHandler now that chatAreaManager is created
        formManager.setMessageHandler(new FormManager.MessageHandler() {
            @Override
            public void addBotMessage(String message) {
                chatAreaManager.addBotMessage(message);
            }
            
            @Override
            public void addBotResponse(String response) {
                chatAreaManager.addBotResponse(response);
            }
            
            @Override
            public void addUserMessage(String message) {
                chatAreaManager.addUserMessage(message);
            }
        });

        // Add initial welcome message
        chatAreaManager.addBotMessage("Hello! I'm your TODO Bot. What can I do for you?");
        if (textInputVisible) {
            chatAreaManager.addBotMessage("Type commands like: todo buy milk, list, help, bye");
        } else {
            // Show main menu buttons
            String mainMenuResponse = service.processButtonClick("");
            chatAreaManager.addBotResponse(mainMenuResponse);
        }
        
        // Ensure initial messages are visible
        scrollToBottom();

        // Set up event handlers
        setupEventHandlers();

        // Create scene and show stage
        Scene scene = new Scene(root, 600, 500);
        
        // Load CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
        
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
            chatAreaManager.addBotMessage("Text input mode enabled. Type commands like: todo buy milk, list, help, bye");
            inputField.requestFocus();
        } else {
            chatAreaManager.addBotMessage("Button mode enabled. Use the buttons below to interact.");
            String mainMenuResponse = service.processButtonClick("");
            chatAreaManager.addBotResponse(mainMenuResponse);
        }
        
        scrollToBottom();
    }

    private void handleSendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // Add user message
            chatAreaManager.addUserMessage(message);
            
            // Clear input field
            inputField.clear();
            
            // Process command through service
            String response = service.processCommand(message);
            
            // Check if bye command after processing
            if (service.shouldExit()) {
                chatAreaManager.addBotMessage("Saving & Closing... Bye. Hope to see you again soon!");
                
                // Close after delay to show message using Timeline (non-blocking)
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> {
                    service.cleanup();
                    primaryStage.close();
                }));
                timeline.play();
            } else {
                // If in button mode, check if we should show buttons after response
                if (!textInputVisible) {
                    chatAreaManager.addBotMessage(response);
                    // Return to main menu after processing command
                    String mainMenuResponse = service.processButtonClick("");
                    chatAreaManager.addBotResponse(mainMenuResponse);
                } else {
                    chatAreaManager.addBotMessage(response);
                }
            }
            
            // Scroll to bottom
            scrollToBottom();
        }
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
        chatAreaManager.addUserMessage("Clicked: " + getButtonLabel(action));
        
        // Handle special cases
        if (action.equals("exit")) {
            chatAreaManager.addBotMessage("Saving & Closing... Bye. Hope to see you again soon!");
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
            chatAreaManager.addBotMessage(response);
            showTextInputForTask(action);
        } else {
            chatAreaManager.addBotResponse(response);
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
                chatAreaManager.addUserMessage(taskText);
                
                // Pass raw data to service - let it handle command building
                String response;
                if (taskType.equals("todo")) {
                    response = service.handleTodoTask(taskText);
                } else if (taskType.equals("find_tasks")) {
                    response = service.handleFindTask(taskText);
                } else {
                    response = "Unknown task type";
                }
                chatAreaManager.addBotMessage(response);
                
                // Remove the input box
                chatArea.getChildren().remove(inputBox);
                
                // Return to main menu
                String mainMenuResponse = service.processButtonClick("");
                chatAreaManager.addBotResponse(mainMenuResponse);
                
                scrollToBottom();
            }
        });
        
        cancelButton.setOnAction(e -> {
            // Remove the input box
            chatArea.getChildren().remove(inputBox);
            
            // Return to main menu
            String mainMenuResponse = service.processButtonClick("");
            chatAreaManager.addBotResponse(mainMenuResponse);
            
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