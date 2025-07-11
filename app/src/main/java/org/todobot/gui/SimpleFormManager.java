package org.todobot.gui;

import javafx.scene.layout.VBox;
import org.todobot.service.ToDoBotService;
import java.util.function.Consumer;

public class SimpleFormManager {
    
    private final ToDoBotService service;
    private final VBox chatArea;
    private final Runnable scrollHandler;
    private final Consumer<String> messageHandler;
    
    public SimpleFormManager(ToDoBotService service, VBox chatArea, 
                           Runnable scrollHandler, Consumer<String> messageHandler) {
        this.service = service;
        this.chatArea = chatArea;
        this.scrollHandler = scrollHandler;
        this.messageHandler = messageHandler;
    }
    
    public void showForm(BotResponse.FormType formType) {
        TaskForm form = new TaskForm(formType, service, chatArea, scrollHandler, messageHandler);
        form.show();
    }
    
    public void showDropdown(int taskCount, String backButton) {
        SimpleDropdownForm dropdown = new SimpleDropdownForm(service, chatArea, messageHandler, scrollHandler);
        dropdown.show(taskCount, backButton);
    }
}