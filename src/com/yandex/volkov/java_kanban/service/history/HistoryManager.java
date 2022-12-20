package com.yandex.volkov.java_kanban.service.history;

import com.yandex.volkov.java_kanban.task.Task;

import java.util.List;


public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();
}





