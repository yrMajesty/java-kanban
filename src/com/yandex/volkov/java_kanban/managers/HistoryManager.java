package com.yandex.volkov.java_kanban.managers;

import com.yandex.volkov.java_kanban.task.Task;

import java.util.List;


public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}





