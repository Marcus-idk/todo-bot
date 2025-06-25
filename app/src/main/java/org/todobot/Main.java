package org.todobot;

public class Main {
    public static void main(String[] args) {
        ToDoBotCLI bot = new ToDoBotCLI();
        try {
            bot.run();
        } finally {
            bot.cleanup();
        }
    }
}