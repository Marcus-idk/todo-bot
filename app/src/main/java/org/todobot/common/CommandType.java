package org.todobot.common;

public enum CommandType {
    TODO("todo"),
    DEADLINE("deadline"), 
    EVENT("event"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    DELETE_ALL("delete_all"),
    DELETE_ALL_CONFIRM("delete_all_confirm"),
    FIND("find"),
    HELP("help"),
    BYE("bye");
    
    private final String command;
    
    CommandType(String command) {
        this.command = command;
    }
    
    public String getCommand() {
        return command;
    }
    
    public static CommandType fromString(String command) {
        for (CommandType type : CommandType.values()) {
            if (type.command.equals(command)) {
                return type;
            }
        }
        return null;
    }
}