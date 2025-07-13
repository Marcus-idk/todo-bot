package org.todobot.app;

import org.todobot.service.ToDoBotService;
import org.todobot.ui.ThemeManager;
import org.todobot.ui.AnimatedMessage;
import org.todobot.ui.AnimatedMessage.MessageType;
import org.todobot.ui.AnimationUtils;
import org.todobot.common.BotMessages;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private ToDoBotService service;
    private Stage primaryStage;
    
    // Processing state management
    private AnimatedMessage processingMessage;
    private Timeline typingAnimation;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.service = new ToDoBotService();
        
        primaryStage.setTitle("Task Manager - KUN_BOT");

        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle(ThemeManager.MAIN_BACKGROUND);
        
        // Create UI components
        createChatArea();
        HBox inputArea = createInputArea();
        
        // Assemble layout
        root.setCenter(scrollPane);
        root.setBottom(inputArea);

        // Add initial messages and setup
        addInitialMessages();
        setupEventHandlers();
        setupScene(root);
    }
    
    private void createChatArea() {
        // Create chat area with glassmorphism container
        chatArea = new VBox(15);
        chatArea.setPadding(new Insets(20));
        chatArea.setAlignment(Pos.TOP_LEFT);
        
        // Add listener to auto-scroll when content height changes
        chatArea.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollToBottom();
        });

        // Create scroll pane for chat area with futuristic styling
        scrollPane = new ScrollPane(chatArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(ThemeManager.SCROLL_PANE);
        
        // Apply glassmorphism effect to chat container
        chatArea.setStyle(ThemeManager.CHAT_CONTAINER);
    }
    
    private HBox createInputArea() {
        // Create futuristic input area
        HBox inputArea = new HBox(15);
        inputArea.setStyle(ThemeManager.INPUT_CONTAINER);
        inputArea.setAlignment(Pos.CENTER);

        // Professional input field with subtle effects
        inputField = new TextField();
        inputField.setPromptText("Enter command (todo, list, help, deadline, event)");
        inputField.setStyle(ThemeManager.INPUT_FIELD + ThemeManager.INPUT_PROMPT_TEXT);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        
        // Add focus effects to input field
        inputField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                inputField.setStyle(ThemeManager.INPUT_FIELD_FOCUSED + ThemeManager.INPUT_PROMPT_TEXT);
            } else {
                inputField.setStyle(ThemeManager.INPUT_FIELD + ThemeManager.INPUT_PROMPT_TEXT);
            }
        });

        // Futuristic send button with hover effects
        sendButton = new Button("SEND");
        sendButton.setStyle(ThemeManager.SEND_BUTTON);
        sendButton.setMinWidth(100);
        
        // Add hover effects to send button
        sendButton.setOnMouseEntered(e -> sendButton.setStyle(ThemeManager.SEND_BUTTON_HOVER));
        sendButton.setOnMouseExited(e -> sendButton.setStyle(ThemeManager.SEND_BUTTON));

        inputArea.getChildren().addAll(inputField, sendButton);
        return inputArea;
    }
    
    private void addInitialMessages() {
        addBotMessage(BotMessages.GUI_SYSTEM_READY);
        addBotMessage(BotMessages.GUI_AVAILABLE_COMMANDS);
        scrollToBottom();
    }
    
    private void setupScene(BorderPane root) {
        // Create scene with optimal size for futuristic interface
        Scene scene = new Scene(root, 800, 600);
        
        // Apply custom scroll bar styling
        scene.getStylesheets().add("data:text/css," + ThemeManager.CUSTOM_SCROLL_BAR);
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        
        // Handle window close event
        primaryStage.setOnCloseRequest(e -> {
            service.cleanup();
        });
        
        primaryStage.show();

        // Focus on input field
        inputField.requestFocus();
    }

    private void setupEventHandlers() {
        sendButton.setOnAction(e -> handleSendMessage());
        inputField.setOnAction(e -> handleSendMessage());
    }

    private void handleSendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // Add user message and clear input
            addUserMessage(message);
            inputField.clear();
            
            // Route to appropriate handler
            if (service.shouldExit(message)) {
                handleByeCommand();
            } else {
                handleRegularCommand(message);
            }
            
            scrollToBottom();
        }
    }
    
    private void handleByeCommand() {
        showProcessingIndicator();
        
        Timeline delay = AnimationUtils.createProcessingDelay();
        delay.setOnFinished(e -> {
            hideProcessingIndicator();
            addBotMessage(BotMessages.GUI_SAVING_GOODBYE);
            
            // Close after additional delay
            Timeline closeDelay = new Timeline(new KeyFrame(Duration.millis(2000), closeEvent -> {
                service.cleanup();
                primaryStage.close();
            }));
            closeDelay.play();
        });
        delay.play();
    }
    
    private void handleRegularCommand(String message) {
        showProcessingIndicator();
        
        Timeline delay = AnimationUtils.createProcessingDelay();
        delay.setOnFinished(e -> {
            hideProcessingIndicator();
            String response = service.processCommand(message);
            addBotMessage(response);
        });
        delay.play();
    }

    private void addUserMessage(String message) {
        chatArea.getChildren().add(new AnimatedMessage(message, MessageType.USER));
    }

    private void addBotMessage(String message) {
        chatArea.getChildren().add(new AnimatedMessage(message, MessageType.BOT));
    }
    
    private void showProcessingIndicator() {
        // Create processing message
        processingMessage = new AnimatedMessage("Processing", MessageType.BOT);
        chatArea.getChildren().add(processingMessage);
        
        // Start typing dots animation on the label inside the processing message
        Label processingLabel = (Label) processingMessage.getChildren().get(0);
        typingAnimation = AnimationUtils.createTypingDots(processingLabel);
        typingAnimation.play();
    }
    
    private void hideProcessingIndicator() {
        if (processingMessage != null) {
            // Stop typing animation
            if (typingAnimation != null) {
                typingAnimation.stop();
                typingAnimation = null;
            }
            
            // Remove processing message from chat area
            chatArea.getChildren().remove(processingMessage);
            processingMessage = null;
        }
    }

    private void scrollToBottom() {
        javafx.application.Platform.runLater(() -> {
            scrollPane.setVvalue(1.0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}