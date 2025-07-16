package org.todobot.ui;

import java.util.Optional;

import org.todobot.service.AutoCompleteService;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 * TextField with auto-complete functionality that shows completion suggestions
 * as gray overlay text. Press Tab to accept, Escape to clear.
 */
public class AutoCompleteTextField extends StackPane {
    private static final double CHAR_WIDTH = 7.2;
    private static final double PADDING = 16;
    
    private final TextField textField;
    private final Label completionOverlay;
    private final AutoCompleteService autoCompleteService;
    private final ChangeListener<String> textChangeListener;
    
    private String currentCompletion = "";
    
    public AutoCompleteTextField(AutoCompleteService autoCompleteService) {
        this.autoCompleteService = autoCompleteService;
        this.textChangeListener = (obs, old, text) -> handleTextChange(text);
        
        this.textField = new TextField();
        this.completionOverlay = createCompletionOverlay();
        
        getChildren().addAll(textField, completionOverlay);
        setupEventHandlers();
    }
    
    private Label createCompletionOverlay() {
        Label overlay = new Label();
        overlay.setStyle(ThemeManager.COMPLETION_OVERLAY);
        overlay.setMouseTransparent(true);
        overlay.setVisible(false);
        return overlay;
    }
    
    private void setupEventHandlers() {
        textField.textProperty().addListener(textChangeListener);
        textField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        textField.layoutBoundsProperty().addListener((obs, old, bounds) -> 
            Platform.runLater(this::updateCompletionDisplay));
        textField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) clearCompletion();
        });
    }
    
    private void handleTextChange(String newText) {
        if (newText == null || newText.isBlank()) {
            clearCompletion();
            return;
        }
        
        // Clear completion if user typed beyond it
        if (!currentCompletion.isEmpty() && 
            !newText.toLowerCase().endsWith(currentCompletion.toLowerCase())) {
            clearCompletion();
        }
        
        // Find and display new completion
        autoCompleteService.getBestCompletion(newText)
            .filter(s -> !s.isBlank())
            .ifPresentOrElse(completion -> {
                currentCompletion = completion;
                updateCompletionDisplay();
            }, this::clearCompletion);
    }
    
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            event.consume();
            acceptCompletion();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            event.consume();
            clearCompletion();
        }
    }
    
    private void updateCompletionDisplay() {
        if (currentCompletion.isEmpty()) {
            completionOverlay.setVisible(false);
            return;
        }
        
        completionOverlay.setText(textField.getText() + currentCompletion);
        completionOverlay.setVisible(true);
        positionOverlay();
    }
    
    private void positionOverlay() {
        if (!completionOverlay.isVisible()) return;
        
        Bounds bounds = textField.getBoundsInParent();
        completionOverlay.setLayoutX(bounds.getMinX());
        completionOverlay.setLayoutY(bounds.getMinY());
        
        // Position after existing text
        String currentText = textField.getText();
        if (currentText != null && !currentText.isEmpty()) {
            double textWidth = currentText.length() * CHAR_WIDTH;
            completionOverlay.setLayoutX(bounds.getMinX() + PADDING + textWidth);
        } else {
            completionOverlay.setLayoutX(bounds.getMinX() + PADDING);
        }
    }
    
    private void acceptCompletion() {
        if (currentCompletion.isEmpty()) return;
        
        textField.textProperty().removeListener(textChangeListener);
        
        String committed = textField.getText() + currentCompletion;
        currentCompletion = "";
        textField.setText(committed);
        textField.positionCaret(committed.length());
        
        textField.textProperty().addListener(textChangeListener);
        clearCompletion();
    }
    
    private void clearCompletion() {
        currentCompletion = "";
        completionOverlay.setVisible(false);
    }
    
    // TextField delegate methods
    
    public void setPromptText(String promptText) { textField.setPromptText(promptText); }
    public String getText() { return textField.getText(); }
    public void setText(String text) { textField.setText(text); }
    public void clear() { textField.clear(); clearCompletion(); }
    public void setTextFieldStyle(String style) { textField.setStyle(style); }
    public void requestFocus() { textField.requestFocus(); }
    public void setOnAction(javafx.event.EventHandler<javafx.event.ActionEvent> handler) { 
        textField.setOnAction(handler); 
    }
    public javafx.beans.property.ReadOnlyBooleanProperty textFieldFocusedProperty() { 
        return textField.focusedProperty(); 
    }
}