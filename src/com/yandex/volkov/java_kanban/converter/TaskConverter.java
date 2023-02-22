package com.yandex.volkov.java_kanban.converter;

import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.task.*;

import java.util.ArrayList;
import java.util.List;

public class TaskConverter {

    private TaskConverter() {
    }

    public static String toString(Task task) {
        String[] toJoin = {Integer.toString(task.getId()), TaskType.TASK.toString(), task.getTitle(), task.getDescriptions(), task.getStatus().toString(),};
        return String.join(",", toJoin) + ',' + null + "\n";
    }

    public static String toString(Epic task) {
        String[] toJoin = {Integer.toString(task.getId()), TaskType.EPIC.toString(), task.getTitle(), task.getDescriptions(), task.getStatus().toString(),};
        return String.join(",", toJoin) + ',' + null + "\n";
    }

    public static String toString(Subtask task) {
        String[] toJoin = {Integer.toString(task.getId()), TaskType.SUBTASK.toString(), task.getTitle(), task.getDescriptions(), task.getStatus().toString(), task.getEpicId().toString()};
        return String.join(",", toJoin) + "\n";
    }

    public static Epic getEpicFromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        String title = split[2];
        String descriptions = split[3];
        Status status = Status.valueOf(split[4].toUpperCase());

        Epic epic = new Epic(title, descriptions, status);
        epic.setId(id);
        epic.setStatus(status);
        return epic;
    }

    public static Subtask getSubTaskFromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        String title = split[2];
        String descriptions = split[3];
        Status status = Status.valueOf(split[4].toUpperCase());
        int epicId = Integer.parseInt(split[5]);

        Subtask subtask = new Subtask(title, descriptions, status, epicId);
        subtask.setId(id);
        return subtask;
    }

    public static TaskType getTypeFromLine(String line) {
        String[] split = line.split(",");
        return TaskType.valueOf(split[1].toUpperCase());
    }

    public static Task getTaskFromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        String title = split[2];
        String descriptions = split[3];
        Status status = Status.valueOf(split[4].toUpperCase());

        Task task = new Task(title, descriptions, status);
        task.setId(id);
        return task;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> stringHistory = new ArrayList<>();
        if (value != null) {
            String[] id = value.split(",");

            for (String number : id) {
                stringHistory.add(Integer.parseInt(number));
            }

            return stringHistory;
        }
        return stringHistory;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder sb = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task task : history) {
            sb.append(task.getId()).append(",");
        }

        if (sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
