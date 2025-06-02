public class TaskList {
    private String[] tasks;
    private int taskCount;
    private static final int MAX_TASKS = 100;
    
    public TaskList() {
        tasks = new String[MAX_TASKS];
        taskCount = 0;
    }
    
    public boolean addTask(String task) {
        if (taskCount >= MAX_TASKS) {
            return false;
        }
        tasks[taskCount] = task;
        taskCount++;
        return true;
    }
    
    public String listTasks() {
        if (taskCount == 0) {
            return "No tasks found! Your to-do list is as empty as my brain! 🤖";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            sb.append(" ").append(i + 1).append(". ").append(tasks[i]);
            if (i < taskCount - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    public boolean isFull() {
        return taskCount >= MAX_TASKS;
    }
    
    public boolean isEmpty() {
        return taskCount == 0;
    }
    
    public int getTaskCount() {
        return taskCount;
    }
}