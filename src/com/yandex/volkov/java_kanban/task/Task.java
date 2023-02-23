package com.yandex.volkov.java_kanban.task;

import com.yandex.volkov.java_kanban.converter.DataConverter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    Integer id;
    String title;
    String descriptions;
    Status status;
    LocalDateTime startTime;
    long durationInMinutes;

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
        this.durationInMinutes = durationInMinutes;
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

    public long getDuration() {
        return durationInMinutes;
    }

    public void setDuration(long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(durationInMinutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return durationInMinutes == task.durationInMinutes && Objects.equals(title, task.title) && Objects.equals(descriptions, task.descriptions) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, descriptions, status, startTime, durationInMinutes);
    }

//    @Override
//    public String toString() {
//        return "Task{" +
//                "id=" + id +
//                ", title='" + getTitle() + '\'' +
//                ", descriptions='" + getDescriptions() + '\'' +
//                ", status=" + status + '\'' +
//                ", startTime='" + DataConverter.DATE_TIME_FORMATTER.format(startTime) + '\'' +
//                ", duration='" + durationInMinutes +
//                '}';
//    }


}
