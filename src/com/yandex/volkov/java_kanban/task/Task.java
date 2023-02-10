package com.yandex.volkov.java_kanban.task;

import java.util.Objects;

public class Task {
    Integer id;
    String title;
    String descriptions;
    Status status;

    public Task(String title, String descriptions, Status status) {
        this.title = title;
        this.descriptions = descriptions;
        this.status = status;
    }

    public Task(int id, String title, String descriptions, Status status) {
        this.id = id;
        this.title = title;
        this.descriptions = descriptions;
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(descriptions, task.descriptions) && Objects.equals(status, task.status);

    }


    @Override
    public String toString() {
        return "Task{" + "id=" + getId() + ", title='" + getTitle() + '\'' + ", descriptions='" + getDescriptions() + '\'' + ", status='" + getStatus() + '\'' + '}';
    }


}
