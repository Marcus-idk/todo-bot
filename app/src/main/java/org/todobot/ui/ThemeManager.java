package org.todobot.ui;

public class ThemeManager {
    
    // === COLOR PALETTE ===
    
    // Background colors - Much darker, industrial
    private static final String BG_PRIMARY = "#000000";           // Pure black
    private static final String BG_SECONDARY = "#0d1117";        // GitHub dark
    private static final String BG_SURFACE = "#161b22";          // Surface dark gray
    private static final String BG_ACCENT = "#21262d";           // Accent surface
    
    // User colors - Muted blue-gray (like VSCode variables)
    private static final String USER_PRIMARY = "#4a9eff";        // Muted blue
    private static final String USER_SECONDARY = "#2d5aa0";      // Dark blue-gray
    private static final String USER_GLOW = "rgba(74, 158, 255, 0.1)"; // Very subtle glow
    
    // Bot colors - Muted purple-gray (professional)
    private static final String BOT_PRIMARY = "#8b949e";         // Muted gray-purple
    private static final String BOT_SECONDARY = "#6e7681";       // Darker gray
    private static final String BOT_GLOW = "rgba(139, 148, 158, 0.1)"; // Very subtle glow
    
    // Text colors - Professional grays
    private static final String TEXT_PRIMARY = "#f0f6fc";        // GitHub text
    private static final String TEXT_SECONDARY = "#8b949e";      // Muted gray
    private static final String TEXT_ACCENT = "#ffffff";         // Pure white
    private static final String TEXT_DIMMED = "#7d8590";         // Dimmed text
    
    // === BACKGROUND STYLES ===
    
    public static final String MAIN_BACKGROUND = 
        "-fx-background-color: linear-gradient(to bottom, " + BG_PRIMARY + ", " + BG_SECONDARY + ");";
    
    public static final String CHAT_CONTAINER = 
        "-fx-background-color: " + BG_SURFACE + "; " +
        "-fx-background-radius: 14; " +
        "-fx-border-color: " + BG_ACCENT + "; " +
        "-fx-border-width: 1; " +
        "-fx-border-radius: 14; " +
        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 20, 0, 0, 4);";
    
    // === MESSAGE STYLES ===
    
    public static final String USER_MESSAGE = 
        "-fx-background-color: linear-gradient(to bottom, " + USER_PRIMARY + ", " + USER_SECONDARY + "); " +
        "-fx-text-fill: " + TEXT_PRIMARY + "; " +
        "-fx-font-family: 'JetBrains Mono', 'SF Mono', 'Monaco', 'Consolas', monospace; " +
        "-fx-font-size: 11px; " +
        "-fx-font-weight: 400; " +
        "-fx-padding: 12 16 12 16; " +
        "-fx-background-radius: 16 16 4 16; " +
        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.12), 12, 0, 0, 3);" +
        "-fx-border-color: " + USER_PRIMARY + "; " +
        "-fx-border-width: 1; " +
        "-fx-border-radius: 16 16 4 16;";
    
    public static final String BOT_MESSAGE = 
        "-fx-background-color: linear-gradient(to bottom, " + BG_ACCENT + ", " + BG_SURFACE + "); " +
        "-fx-text-fill: " + TEXT_PRIMARY + "; " +
        "-fx-font-family: 'JetBrains Mono', 'SF Mono', 'Monaco', 'Consolas', monospace; " +
        "-fx-font-size: 11px; " +
        "-fx-font-weight: 400; " +
        "-fx-padding: 12 16 12 16; " +
        "-fx-background-radius: 16 16 16 4; " +
        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.12), 12, 0, 0, 3);" +
        "-fx-border-color: " + BOT_PRIMARY + "; " +
        "-fx-border-width: 1; " +
        "-fx-border-radius: 16 16 16 4;";
    
    // === INPUT AREA STYLES ===
    
    public static final String INPUT_FIELD = 
        "-fx-background-color: " + BG_PRIMARY + "; " +
        "-fx-text-fill: " + TEXT_PRIMARY + "; " +
        "-fx-font-family: 'JetBrains Mono', 'SF Mono', 'Monaco', 'Consolas', monospace; " +
        "-fx-font-size: 12px; " +
        "-fx-border-color: " + BG_ACCENT + "; " +
        "-fx-border-width: 1; " +
        "-fx-border-radius: 12; " +
        "-fx-background-radius: 12; " +
        "-fx-padding: 12 18 12 18; " +
        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 8, 0, 0, 2);";
    
    public static final String INPUT_FIELD_FOCUSED = 
        "-fx-background-color: " + BG_PRIMARY + "; " +
        "-fx-text-fill: " + TEXT_PRIMARY + "; " +
        "-fx-font-family: 'JetBrains Mono', 'SF Mono', 'Monaco', 'Consolas', monospace; " +
        "-fx-font-size: 12px; " +
        "-fx-border-color: " + USER_PRIMARY + "; " +
        "-fx-border-width: 1; " +
        "-fx-border-radius: 12; " +
        "-fx-background-radius: 12; " +
        "-fx-padding: 12 18 12 18; " +
        "-fx-effect: dropshadow(gaussian, " + USER_GLOW + ", 10, 0, 0, 2);";
    
    public static final String SEND_BUTTON = 
        "-fx-background-color: linear-gradient(to bottom, " + USER_PRIMARY + ", " + USER_SECONDARY + "); " +
        "-fx-text-fill: " + TEXT_PRIMARY + "; " +
        "-fx-font-family: 'JetBrains Mono', 'SF Mono', 'Monaco', 'Consolas', monospace; " +
        "-fx-font-size: 11px; " +
        "-fx-font-weight: 500; " +
        "-fx-background-radius: 12; " +
        "-fx-border-radius: 12; " +
        "-fx-border-color: " + USER_PRIMARY + "; " +
        "-fx-border-width: 1; " +
        "-fx-padding: 12 18 12 18; " +
        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 8, 0, 0, 2);";
    
    public static final String SEND_BUTTON_HOVER = 
        "-fx-background-color: linear-gradient(to bottom, " + USER_PRIMARY + ", " + USER_PRIMARY + "); " +
        "-fx-text-fill: " + TEXT_PRIMARY + "; " +
        "-fx-font-family: 'JetBrains Mono', 'SF Mono', 'Monaco', 'Consolas', monospace; " +
        "-fx-font-size: 11px; " +
        "-fx-font-weight: 500; " +
        "-fx-background-radius: 12; " +
        "-fx-border-radius: 12; " +
        "-fx-border-color: " + USER_PRIMARY + "; " +
        "-fx-border-width: 1; " +
        "-fx-padding: 12 18 12 18; " +
        "-fx-effect: dropshadow(gaussian, " + USER_GLOW + ", 12, 0, 0, 3);";
    
    // === CONTAINER STYLES ===
    
    public static final String INPUT_CONTAINER = 
        "-fx-background-color: " + BG_SECONDARY + "; " +
        "-fx-background-radius: 0 0 14 14; " +
        "-fx-padding: 18; " +
        "-fx-border-color: " + BG_ACCENT + "; " +
        "-fx-border-width: 1 0 0 0;";
    
    public static final String SCROLL_PANE = 
        "-fx-background-color: transparent; " +
        "-fx-background: transparent; " +
        "-fx-border-color: transparent;";
    
    // === SCROLL BAR STYLING ===
    
    public static final String SCROLL_BAR_VERTICAL = 
        ".scroll-bar:vertical .track {" +
        "    -fx-background-color: " + BG_SURFACE + ";" +
        "    -fx-border-color: transparent;" +
        "    -fx-background-radius: 4;" +
        "}" +
        ".scroll-bar:vertical .thumb {" +
        "    -fx-background-color: " + BG_ACCENT + ";" +
        "    -fx-background-radius: 4;" +
        "}" +
        ".scroll-bar:vertical .thumb:hover {" +
        "    -fx-background-color: " + TEXT_SECONDARY + ";" +
        "}" +
        ".scroll-bar:vertical .increment-button, .scroll-bar:vertical .decrement-button {" +
        "    -fx-background-color: transparent;" +
        "    -fx-border-color: transparent;" +
        "}" +
        ".scroll-bar:vertical {" +
        "    -fx-background-color: transparent;" +
        "}";
    
    public static final String SCROLL_BAR_CSS = 
        ".scroll-pane > .viewport {" +
        "    -fx-background-color: transparent;" +
        "}" +
        ".scroll-pane {" +
        "    -fx-background-color: transparent;" +
        "    -fx-border-color: transparent;" +
        "}" +
        ".scroll-pane .corner {" +
        "    -fx-background-color: transparent;" +
        "}";
        
    public static final String CUSTOM_SCROLL_BAR = SCROLL_BAR_VERTICAL + SCROLL_BAR_CSS;
    
    // === PROMPT TEXT ===
    
    public static final String INPUT_PROMPT_TEXT = 
        "-fx-prompt-text-fill: " + TEXT_DIMMED + ";";
    
}