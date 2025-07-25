package org.todobot.app;

import org.todobot.service.ToDoBotService;
import org.todobot.ui.UI;

public class ToDoBotCLI {
    private final ToDoBotService service;
    private final UI ui;
    
    public ToDoBotCLI() {
        this.service = new ToDoBotService();
        this.ui = new UI();
    }
    
    private void handleUserInput() {
        while (true) {
            String input = ui.readCommand();
            
            if (service.shouldExit(input)) {
                break;
            }
            
            String response = service.processCommand(input);
            ui.showResponse(response);
            ui.showLine();
        }
    }
    
    public void run() {
        ui.showGreeting();
        handleUserInput();
        ui.showFarewell();
    }
    
    public void cleanup() {
        service.cleanup();
        ui.close();
    }
}