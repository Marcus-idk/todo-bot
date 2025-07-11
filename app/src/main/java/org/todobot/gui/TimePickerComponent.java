package org.todobot.gui;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TimePickerComponent {
    private final ComboBox<String> hourBox;
    private final ComboBox<String> minuteBox;
    
    public TimePickerComponent(String defaultHour, String defaultMinute) {
        this.hourBox = new ComboBox<>();
        this.minuteBox = new ComboBox<>();
        
        // Populate hour dropdown (00-23)
        for (int i = 0; i < 24; i++) {
            hourBox.getItems().add(String.format("%02d", i));
        }
        hourBox.setValue(defaultHour);
        
        // Populate minute dropdown (00-59)
        for (int i = 0; i < 60; i++) {
            minuteBox.getItems().add(String.format("%02d", i));
        }
        minuteBox.setValue(defaultMinute);
    }
    
    public HBox createTimeBox() {
        HBox timeBox = new HBox(10);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        timeBox.getChildren().addAll(hourBox, new Label(":"), minuteBox);
        return timeBox;
    }
    
    public String getHour() {
        return hourBox.getValue();
    }
    
    public String getMinute() {
        return minuteBox.getValue();
    }
}