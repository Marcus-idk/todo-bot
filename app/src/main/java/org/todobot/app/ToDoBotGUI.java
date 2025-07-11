package org.todobot.app;

import java.time.LocalDate;

import org.todobot.gui.SimpleChatAreaManager;
import org.todobot.gui.SimpleFormManager;
import org.todobot.gui.BotResponse;
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
    private boolean textInputVisible = false;
    private ToDoBotService service;
    private Stage primaryStage;
    private SimpleChatAreaManager chatAreaManager;
    private SimpleFormManager formManager;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.service = new ToDoBotService();
        
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

        // Initialize simplified managers
        formManager = new SimpleFormManager(service, chatArea, this::scrollToBottom, this::addBotMessage);
        chatAreaManager = new SimpleChatAreaManager(chatArea, service, formManager, this::scrollToBottom, this::handleExit);

        // Add initial welcome message
        chatAreaManager.addBotMessage("Hello! I'm your TODO Bot. What can I do for you?");
        if (textInputVisible) {
            chatAreaManager.addBotMessage("Type commands like: todo buy milk, list, help, bye");
        } else {
            // Show main menu buttons
            BotResponse mainMenuResponse = service.processButtonClick("");
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
            BotResponse mainMenuResponse = service.processButtonClick("");
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
                    BotResponse mainMenuResponse = service.processButtonClick("");
                    chatAreaManager.addBotResponse(mainMenuResponse);
                } else {
                    chatAreaManager.addBotMessage(response);
                }
            }
            
            // Scroll to bottom
            scrollToBottom();
        }
    }

    private void handleExit(String action) {
        if (action.equals("exit")) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> {
                service.cleanup();
                primaryStage.close();
            }));
            timeline.play();
        }
    }

    private void addBotMessage(String message) {
        chatAreaManager.addBotMessage(message);
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