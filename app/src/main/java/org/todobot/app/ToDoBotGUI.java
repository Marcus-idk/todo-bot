package org.todobot.app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.todobot.service.ToDoBotService;

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
        
        // Set initial mode to match GUI state
        service.setTextInputMode(textInputVisible);
        
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
            addBotMessage("Use the arrow button below to toggle text input, or interact with the buttons I'll show you.");
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
        
        // Notify service of mode change
        service.setTextInputMode(textInputVisible);
        
        // Focus on input field if shown
        if (textInputVisible) {
            inputField.requestFocus();
        }
    }

    private void handleSendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // Add user message
            addUserMessage(message);
            
            // Clear input field
            inputField.clear();
            
            // Check if bye command
            if (service.shouldExit(message)) {
                addBotMessage("Saving & Closing... Bye. Hope to see you again soon!");
                
                // Close after delay to show message using Timeline (non-blocking)
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> {
                    service.cleanup();
                    primaryStage.close();
                }));
                timeline.play();
            } else {
                // Process command through service
                String response = service.processCommand(message);
                addBotMessage(response);
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