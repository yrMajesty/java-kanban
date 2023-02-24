package com.yandex.volkov.java_kanban.web;

import com.yandex.volkov.java_kanban.exceptions.HttpException;
import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.TaskManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest {
    private static TaskManager httpManager;
    private static KVServer kvServer;
    private static String urlKVServer;
    private HttpTaskManager loadedManager;

    private Task task;
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void init() {
        try {
            urlKVServer = KVServer.URL;
            kvServer = new KVServer();
            kvServer.start();
            httpManager = Manager.getDefault();
        } catch (IOException e) {
            throw new HttpException("Ошибка при запуске сервера");
        }

        task = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 2, 1, 10, 0, 0), 10);

        epic = new Epic("Title Epic", "Descr Epic", Status.NEW,
                LocalDateTime.of(2023, 12, 1, 8, 0, 0), 60);

        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 13, 0, 0), 60);
    }

    @AfterEach
    void stopServer() {
        kvServer.stop();
    }

    void createAndLoadManager(){
        loadedManager = new HttpTaskManager(urlKVServer);
        loadedManager.load();
    }

    @Test
    void loadManager_historyEqualsSavedHistory_createTaskAndLoadManagerWithEmptyHistory() {
        httpManager.addNewTask(task);

        createAndLoadManager();

        List<Task> loadedHistory = loadedManager.getHistory();

        assertEquals(httpManager.getHistory(), loadedHistory, "Сохраненная и восстановленная история не равны");
    }

    @Test
    void loadManager_historyEqualsSavedHistory_createAndViewedTaskAndLoadManager() {
        httpManager.addNewTask(task);
        httpManager.getTask(task.getId());

        createAndLoadManager();

        List<Task> loadedHistory = loadedManager.getHistory();

        assertEquals(httpManager.getHistory(), loadedHistory, "Сохраненная и восстановленная история не равны");
    }

    @Test
    void loadManager_loadableTaskEqualsSavedTask_createTaskAndLoadManager() {
        httpManager.addNewTask(task);

        createAndLoadManager();

        Task loadedTask = loadedManager.getTask(task.getId());
        assertEquals(task, loadedTask, "Восстановленная и созданная задачи не равны");
    }

    @Test
    void loadManager_loadableEpicEqualsSavedEpic_createEpicAndLoadManager() {
        httpManager.addNewEpic(epic);

        createAndLoadManager();

        Epic loadedEpic = loadedManager.getEpic(epic.getId());

        assertEquals(epic, loadedEpic, "Восстановленный и созданный эпик не равны");
    }

    @Test
    void loadManager_loadableEpicEqualsSavedEpicWithSubtasks_createEpicAndLoadManager() {
        httpManager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        httpManager.addNewSubtask(subtask1);
        httpManager.addNewSubtask(subtask2);
        List<Subtask> createdSubtasks = new ArrayList<>();
        createdSubtasks.add(subtask1);
        createdSubtasks.add(subtask2);

        createAndLoadManager();

        Epic loadedEpic = loadedManager.getEpic(epic.getId());
        List<Subtask> loadedSubtasks = new ArrayList<>(loadedManager.getAllSubtaskInEpic(epic.getId()));

        assertEquals(createdSubtasks, loadedSubtasks, "Восстановленные и созданные подзадачи не равны");
        assertEquals(epic, loadedEpic, "Восстановленный созданный эпик не равны");
    }
}
