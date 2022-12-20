package com.yandex.volkov.java_kanban.service.manager;

import com.yandex.volkov.java_kanban.service.history.HistoryManager;
import com.yandex.volkov.java_kanban.service.history.InMemoryHistoryManager;
import com.yandex.volkov.java_kanban.service.history.InMemoryTaskManager;


public class Manager {

    public static TaskManager  getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();

    }


}
