package com.yandex.volkov.java_kanban.managers;

import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.managers.history.InMemoryHistoryManager;
import com.yandex.volkov.java_kanban.managers.task.InMemoryTaskManager;
import com.yandex.volkov.java_kanban.managers.task.TaskManager;


public class Manager {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();

    }

}
