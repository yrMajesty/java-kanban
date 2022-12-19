package service;

import task.Task;

import java.util.List;

public class Manager {

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static List<Task> getDefaultHistory() {
        return new InMemoryHistoryManager().getHistory();

    }


}
