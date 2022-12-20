package com.yandex.volkov.java_kanban.task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(Integer id, String title, String descriptions, Status status) {
        super(id, title, descriptions, status);
    }

    public Subtask(String title, String descriptions, Status status) {
        super(title, descriptions, status);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic_id=" + getEpicId() +
                ", id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", descriptions='" + getDescriptions() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }

}
