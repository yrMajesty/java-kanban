package com.yandex.volkov.java_kanban.task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int epicId, String title, String descriptions, Status status) {
        super(title, descriptions, status);
        this.epicId = epicId;
    }

    public Subtask(int epicId, int id, String title, String descriptions, Status status) {
        super(id, title, descriptions, status);
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
