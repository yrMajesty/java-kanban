package com.yandex.volkov.java_kanban.service.history;

import com.yandex.volkov.java_kanban.task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        final int maxSize = 10;
        if (historyList.size() >= maxSize) {
            historyList.remove(0);
            historyList.add(task);

        } else {
            historyList.add(task);
        }
    }
}




