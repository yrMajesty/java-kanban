package com.yandex.volkov.java_kanban.task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(Integer id, String title, String descriptions, Status status) {
        super(id, title, descriptions, status);
    }

    public Epic(String title, String descriptions, Status status) {

        super(title, descriptions, status);
    }

    public boolean isEpic() {

        return true;
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subId) {
        subtaskId.add(subId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", descriptions='" + getDescriptions() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
