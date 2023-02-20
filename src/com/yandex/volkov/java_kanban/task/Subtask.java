package com.yandex.volkov.java_kanban.task;

public class Subtask extends Task {

    private final int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" + "epic_id=" + getEpicId() + ", id=" + getId() + ", title='" + getTitle() + '\'' + ", descriptions='" + getDescriptions() + '\'' + ", status='" + getStatus() + '\'' + '}';
    }

}
