package org.todobot.commands;

import org.todobot.TaskList;

public class HelpCommand extends Command {
    
    public HelpCommand(TaskList taskList) {
        super(taskList);
    }
    
    @Override
    public String execute(String[] arguments) {
        StringBuilder help = new StringBuilder();
        help.append(" Available commands:\n\n");
        
        help.append(" Adding tasks:\n");
        help.append("   todo [description]                     - Add a simple todo task\n");
        help.append("   deadline [description] /by [date]      - Add task with deadline\n");
        help.append("   event [description] /from [start] /to [end] - Add event with time range\n\n");
        
        help.append(" Managing tasks:\n");
        help.append("   list, ls, show, display                - Show all tasks\n");
        help.append("   mark [number]                          - Mark task as done\n");
        help.append("   unmark [number]                        - Mark task as not done\n\n");
        
        help.append(" Other:\n");
        help.append("   help, h, ?, commands                   - Show this help message\n");
        help.append("   bye, exit, quit, goodbye, done         - Exit the program\n\n");
        
        help.append(" Examples:\n");
        help.append("   todo read a book\n");
        help.append("   deadline submit report /by Friday 5pm\n");
        help.append("   event team meeting /from Mon 2pm /to 4pm\n");
        help.append("   mark 1\n");
        help.append("   list");
        
        return help.toString();
    }
}