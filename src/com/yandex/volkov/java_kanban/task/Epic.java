package com.yandex.volkov.java_kanban.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subtasks = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public Epic(int id, String title, String descriptions, Status status) {
        super(id, title, descriptions, status);
    }

    public List<Integer> getSubtaskId() {
        return subtasks;
    }

    public void setSubtaskId(int id) {
        subtasks.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" + "id=" + getId() + ", title='" + getTitle() + '\'' + ", descriptions='" + getDescriptions() + '\'' + ", status='" + getStatus() + '\'' + '}';
    }
}
