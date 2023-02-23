package com.yandex.volkov.java_kanban.task;

import com.yandex.volkov.java_kanban.converter.DataConverter;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String title, String descriptions, Status status, int epicId) {
        super(title, descriptions,  status);
        this.epicId = epicId;
    }

    public Subtask(String title, String descriptions,
                   Status status, int epicId, LocalDateTime startTime, long duration) {
        super(title, descriptions,  status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String title, String descriptions,
                   Status status,  LocalDateTime startTime, long duration) {
        super(title, descriptions,  status, startTime, duration);
        this.epicId = 0;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }
//
//    @Override
//    public String toString() {
//        return "Subtask{" +
//                "epicId=" + getEpicId() +
//                ", id=" + getId() +
//                ", title='" + getTitle() + '\'' +
//                ", descriptions='" + getDescriptions() + '\'' +
//                ", status=" + getStatus() + '\'' +
//                ", startTime='" + getStartTime().format(DataConverter.DATE_TIME_FORMATTER) + '\'' +
//                ", endTime='" + getEndTime().format(DataConverter.DATE_TIME_FORMATTER) + '\'' +
//                ", duration='" + getDuration() +
//                '}';
//    }

}
