package com.yandex.volkov.java_kanban.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private LocalDateTime endTime;

    private final List<Integer> subtasks = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public Epic(String title, String descriptions, Status status, LocalDateTime startTime, long duration) {
        super(title, descriptions, status, startTime, duration);
    }

    public List<Integer> getSubtaskId() {
        return subtasks;
    }

    public void setSubtaskId(int id) {
        subtasks.add(id);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
