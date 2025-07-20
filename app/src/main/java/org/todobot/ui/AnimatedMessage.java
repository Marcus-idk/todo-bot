package org.todobot.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Self-animating message component that handles styling, layout, and animations.
 * Replaces manual Label + HBox + styling + animation setup.
 */
public class AnimatedMessage extends HBox {
    
    public enum MessageType {
        USER, BOT
    }
    
    public AnimatedMessage(String message, MessageType type) {
        Label label;
        
        if (type == MessageType.USER) {
            // Create user message with styling
            label = new Label("> " + message);
            label.setStyle(ThemeManager.USER_MESSAGE);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER_RIGHT);
            
            // Configure HBox for user message (right-aligned)
            this.setAlignment(Pos.CENTER_RIGHT);
            this.setPadding(new Insets(4, 0, 4, 60));
            this.getChildren().add(label);
            
            // Start animation after construction is complete
            Platform.runLater(() -> 
                AnimationUtils.slideInFromRight(this).play()
            );
            
        } else { // MessageType.BOT
            // Create bot message with styling
            label = new Label("● " + message);
            label.setStyle(ThemeManager.BOT_MESSAGE);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER_LEFT);
            
            // Configure HBox for bot message (left-aligned)
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(4, 60, 4, 0));
            this.getChildren().add(label);
            
            // Start animation after construction is complete
            Platform.runLater(() -> 
                AnimationUtils.slideInFromLeft(this).play()
            );
        }
    }
}