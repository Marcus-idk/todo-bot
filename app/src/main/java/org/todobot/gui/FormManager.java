package org.todobot.gui;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

public class FormManager {
    
    public interface ServiceProvider {
        String handleDeadlineTask(String description, LocalDate date, String hour, String minute);
        String handleEventTask(String description, LocalDate fromDate, String fromHour, String fromMinute, LocalDate toDate, String toHour, String toMinute);
        String handleDropdownSelection(String selectedTask, String selectedAction);
        String handleTodoTask(String description);
        String handleFindTask(String searchTerm);
        String processButtonClick(String action);
    }
    
    public interface ChatAreaProvider {
        void addChild(VBox formBox);
        void removeChild(VBox formBox);
    }
    
    public interface MessageHandler {
        void addBotMessage(String message);
        void addBotResponse(String response);
        void addUserMessage(String message);
    }
    
    public interface ScrollHandler {
        void scrollToBottom();
    }
    
    public interface FocusHandler {
        void requestFocus(TextField field);
    }
    
    private final ServiceProvider serviceProvider;
    private final ChatAreaProvider chatAreaProvider;
    private MessageHandler messageHandler;
    private final ScrollHandler scrollHandler;
    private final FocusHandler focusHandler;
    
    public FormManager(ServiceProvider serviceProvider, ChatAreaProvider chatAreaProvider, 
                      ScrollHandler scrollHandler, FocusHandler focusHandler) {
        this.serviceProvider = serviceProvider;
        this.chatAreaProvider = chatAreaProvider;
        this.scrollHandler = scrollHandler;
        this.focusHandler = focusHandler;
    }
    
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    
    public void showDeadlineForm() {
        DeadlineForm deadlineForm = new DeadlineForm(serviceProvider, chatAreaProvider, messageHandler, scrollHandler, focusHandler);
        deadlineForm.show();
    }
    
    public void showEventForm() {
        EventForm eventForm = new EventForm(serviceProvider, chatAreaProvider, messageHandler, scrollHandler, focusHandler);
        eventForm.show();
    }
    
    public void showDropdown(int taskCount, String backButton) {
        DropdownForm dropdownForm = new DropdownForm(serviceProvider, chatAreaProvider, messageHandler, scrollHandler);
        dropdownForm.show(taskCount, backButton);
    }
    
    public void showTodoForm() {
        TodoForm todoForm = new TodoForm(serviceProvider, chatAreaProvider, messageHandler, scrollHandler, focusHandler);
        todoForm.show();
    }
    
    public void showFindForm() {
        FindForm findForm = new FindForm(serviceProvider, chatAreaProvider, messageHandler, scrollHandler, focusHandler);
        findForm.show();
    }
}