import java.util.Scanner;

public class ToDoBotCLI {
    private static final String LOGO = " _  ___   _ _   _    ____   ___ _____ \n"
            + "| |/ / | | | \\ | |  | __ ) / _ \\_   _|\n"
            + "| ' /| | | |  \\| |  |  _ \\| | | || |  \n"
            + "| . \\| |_| | |\\  |  | |_) | |_| || |  \n"
            + "|_|\\_\\\\___/|_| \\_|  |____/ \\___/ |_|  \n";
    
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    
    private static void printGreeting() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(LOGO);
        System.out.println(" What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }
    
    private static void handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (!(input = scanner.nextLine().trim()).equals("bye")) {
            System.out.println();
            System.out.println("Bot: " + input);
            System.out.println(HORIZONTAL_LINE);
        }
        scanner.close();
    }
    
    private static void printFarewell() {
        System.out.println();
        System.out.println("Bot: Bye. Hope to see you again soon!");
        System.out.println(HORIZONTAL_LINE);
    }
    
    public static void main(String[] args) {
        printGreeting();
        handleUserInput();
        printFarewell();
    }
}