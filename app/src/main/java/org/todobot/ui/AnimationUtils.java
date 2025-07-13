package org.todobot.ui;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Utility class for creating smooth UI animations.
 * Provides reusable animation methods for consistent UX across the application.
 */
public class AnimationUtils {
    
    // === FIXED ANIMATION SETTINGS ===
    
    private static final double SLIDE_DISTANCE = 80.0;
    private static final Interpolator SMOOTH_EASE = Interpolator.EASE_OUT;
    private static final Duration MESSAGE_DURATION = Duration.millis(250);
    private static final Duration FADE_DURATION = Duration.millis(200);
    
    // === SLIDE ANIMATIONS ===
    
    public static Timeline slideInFromRight(Node node) {
        node.setTranslateX(SLIDE_DISTANCE);
        node.setOpacity(0.0);
        
        KeyValue translateValue = new KeyValue(node.translateXProperty(), 0, SMOOTH_EASE);
        KeyValue opacityValue = new KeyValue(node.opacityProperty(), 1.0, SMOOTH_EASE);
        KeyFrame endFrame = new KeyFrame(MESSAGE_DURATION, translateValue, opacityValue);
        
        return new Timeline(endFrame);
    }
    
    public static Timeline slideInFromLeft(Node node) {
        node.setTranslateX(-SLIDE_DISTANCE);
        node.setOpacity(0.0);
        
        KeyValue translateValue = new KeyValue(node.translateXProperty(), 0, SMOOTH_EASE);
        KeyValue opacityValue = new KeyValue(node.opacityProperty(), 1.0, SMOOTH_EASE);
        KeyFrame endFrame = new KeyFrame(MESSAGE_DURATION, translateValue, opacityValue);
        
        return new Timeline(endFrame);
    }
    
    // === FADE ANIMATIONS ===

    public static Timeline fadeIn(Node node) {
        node.setOpacity(0.0);
        
        KeyValue opacityValue = new KeyValue(node.opacityProperty(), 1.0, SMOOTH_EASE);
        KeyFrame endFrame = new KeyFrame(FADE_DURATION, opacityValue);
        
        return new Timeline(endFrame);
    }
    
    public static Timeline fadeOut(Node node) {
        KeyValue opacityValue = new KeyValue(node.opacityProperty(), 0.0, SMOOTH_EASE);
        KeyFrame endFrame = new KeyFrame(FADE_DURATION, opacityValue);
        
        return new Timeline(endFrame);
    }
    
    // === TYPING INDICATOR ANIMATION ===
    
    public static Timeline createTypingDots(Label label) {
        final String baseText = "Processing";
        final String[] phases = {
            baseText,
            baseText + ".",
            baseText + "..",
            baseText + "..."
        };
        
        Timeline typingAnimation = new Timeline();
        
        // Create keyframes for each dot phase (200ms each = 800ms cycle)
        for (int i = 0; i < phases.length; i++) {
            Duration time = Duration.millis(i * 200);
            String text = phases[i];
            
            KeyFrame frame = new KeyFrame(time, e -> label.setText(text));
            typingAnimation.getKeyFrames().add(frame);
        }
        
        // Make it cycle indefinitely
        typingAnimation.setCycleCount(Timeline.INDEFINITE);
        
        return typingAnimation;
    }
    
    // === DELAY UTILITY ===
    
    public static Timeline createDelay(Duration duration) {
        return new Timeline(new KeyFrame(duration));
    }
    
    public static Timeline createProcessingDelay() {
        // Random delay between 100ms and 1000ms
        double randomSeconds = 0.1 + (Math.random() * 0.9);
        return createDelay(Duration.seconds(randomSeconds));
    }
}