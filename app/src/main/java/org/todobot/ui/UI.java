package org.todobot.ui;

import java.util.Scanner;

import org.todobot.BotMessages;

public class UI {
    private final Scanner scanner;
    
    public UI() {
        this.scanner = new Scanner(System.in);
    }
    
    public void showGreeting() {
        showLine();
        System.out.println(BotMessages.LOGO);
        System.out.println(BotMessages.GREETING);
        showLine();
    }
    
    public void showFarewell() {
        showLine();
        System.out.println(BotMessages.FAREWELL);
        showLine();
    }
    
    public String readCommand() {
        return scanner.nextLine();
    }
    
    public void showResponse(String message) {
        System.out.println(message);
    }
    
    public void showLine() {
        System.out.println(BotMessages.HORIZONTAL_LINE);
    }
    
    public void close() {
        scanner.close();
    }
}