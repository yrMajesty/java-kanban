package com.yandex.volkov.java_kanban.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yandex.volkov.java_kanban.adapters.DurationAdapter;
import com.yandex.volkov.java_kanban.adapters.LocalDateTimeAdapter;
import com.yandex.volkov.java_kanban.managers.FileBackedTasksManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager {
    private final static Gson GSON;
    private final KVTaskClient kvTaskClient;

    static {
        GSON = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();
    }

    public HttpTaskManager(String urlKVServer) {
        kvTaskClient = new KVTaskClient(urlKVServer);
    }

    public void load() {
        String strTasks = kvTaskClient.load("tasks");
        Collection<Task> tasks = GSON.fromJson(strTasks, new TypeToken<Collection<Task>>() {}.getType());
        for (Task task : tasks) {
            taskMap.put(task.getId(), task);
        }

        String strEpics = kvTaskClient.load("epics");
        Collection<Epic> epics = GSON.fromJson(strEpics, new TypeToken<Collection<Epic>>(){}.getType());
        for (Epic epic : epics) {
            epicMap.put(epic.getId(), epic);
        }

        String strSubtasks = kvTaskClient.load("subtasks");
        Collection<Subtask> subtasks = GSON.fromJson(strSubtasks, new TypeToken<Collection<Subtask>>(){}.getType());
        for (Subtask subtask : subtasks) {
            subtaskMap.put(subtask.getId(), subtask);
        }

        String strHistory = kvTaskClient.load("history");
        List<Task> historyList = GSON.fromJson(strHistory, new TypeToken<List<Task>>(){}.getType());
        for (Task task : historyList) {
            historyManager.add(task);
        }
    }

    @Override
    public void save() {
        String strTaskData = GSON.toJson(getAllTasks());
        String strEpicData = GSON.toJson(getAllEpics());
        String strSubtaskData = GSON.toJson(getAllSubtasks());
        String strHistory = GSON.toJson(historyManager.getHistory());

        kvTaskClient.put("tasks", strTaskData);
        kvTaskClient.put("epics", strEpicData);
        kvTaskClient.put("subtasks", strSubtaskData);
        kvTaskClient.put("history", strHistory);
    }
}
