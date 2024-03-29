package com.yandex.volkov.java_kanban.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private Integer id;
    private String title;
    private String descriptions;
    private Status status;
    private LocalDateTime startTime;
    private Duration durationInMinutes;

    public Task(String title, String descriptions, Status status) {
        this.descriptions = descriptions;
        this.title = title;
        this.status = status;
    }

    public Task(String title, String descriptions, Status status, LocalDateTime startTime, long durationInMinutes) {
        this.descriptions = descriptions;
        this.title = title;
        this.status = status;
        this.startTime = startTime;
        this.durationInMinutes = Duration.ofMinutes(durationInMinutes);
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return durationInMinutes;
    }

    public void setDuration(Duration duration) {
        this.durationInMinutes = duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(durationInMinutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(descriptions, task.descriptions) && status == task.status && Objects.equals(startTime, task.startTime) && Objects.equals(durationInMinutes, task.durationInMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, descriptions, status, startTime, durationInMinutes);
    }
}