package com.yandex.volkov.java_kanban.managers;

import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackendTasksManagerTest extends TaskManagerTest<TaskManager> {
    File FILE_TEST = new File("testFile.csv");
    FileBackedTasksManager managerFromFile;

    @BeforeEach
    void initManager() {
        manager = new FileBackedTasksManager(Manager.getDefaultHistory(), FILE_TEST);
        if (Files.notExists(Path.of(FILE_TEST.getPath()))) {
            try {
                Files.createFile(Path.of(FILE_TEST.getPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AfterEach
    void deleteFileAfterEachMethod() {
        try {
            Files.delete(Path.of(FILE_TEST.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createManagerFromFile(){
        managerFromFile = FileBackedTasksManager.loadedFromFileTasksManager(Manager.getDefaultHistory(), FILE_TEST);
        managerFromFile.loadFromFile();
    }

    public void addTasks(){
        manager.addNewTask(task);
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
    }

    @Test
    void createManagerFromFile_rReturnEmptyAllListsTasks_allListsTasksAreEmpty() {
        createManagerFromFile();

        List<Epic> epics = new ArrayList<>(managerFromFile.getAllEpics());
        List<Task> tasks = new ArrayList<>(managerFromFile.getAllTasks());
        List<Subtask> subtasks = new ArrayList<>(managerFromFile.getAllSubtasks());

        assertTrue(epics.isEmpty(), "При чтении из  файла список эпиков не пустой");
        assertTrue(subtasks.isEmpty(), "При чтении из  файла список подзадач не пустой");
        assertTrue(tasks.isEmpty(), "При чтении из  файла список задач не пустой");
    }

    @Test
    void createManagerFromFile_returnRightSizeListsTaskAndHistory_listsTasksAndHistoryIsNotEmpty() {
        addTasks();

        manager.getTask(task.getId());
        manager.getSubtask(subtask1.getId());

        List<Epic> epics = new ArrayList<>(manager.getAllEpics());
        List<Subtask> subtasks = new ArrayList<>(manager.getAllSubtasks());
        List<Task> tasks = new ArrayList<>(manager.getAllTasks());
        List<Task> history = new ArrayList<>(manager.getHistory());

        assertEquals(1, tasks.size());
        assertEquals(2, subtasks.size());
        assertEquals(1, epics.size());
        assertEquals(2, history.size());

        createManagerFromFile();

        List<Epic> epicsFromFile = new ArrayList<>(managerFromFile.getAllEpics());
        List<Subtask> subtasksFromFile = new ArrayList<>(managerFromFile.getAllSubtasks());
        List<Task> tasksFromFile = new ArrayList<>(managerFromFile.getAllTasks());
        List<Task> historyFromFile = new ArrayList<>(managerFromFile.getHistory());

        assertEquals(epicsFromFile.size(), epics.size(),
                "Размер загруженного списка эпиков не равен размеру сохраненного списка");
        assertEquals(subtasksFromFile.size(), subtasks.size(),
                "Размер загруженного списка подзадач не равен размеру сохраненного списка");
        assertEquals(tasksFromFile.size(), tasks.size(),
                "Размер загруженного списка задач не равен размеру сохраненного списка");
        assertEquals(historyFromFile.size(), history.size(),
                "Размер загруженного списка истории не равен размеру сохраненного списка");
    }

    @Test
    void createManagerFromFile_returnRightSizeListsTaskAndHistory_listsTasksIsNotEmptyAndHistoryIsEmpty() {
        addTasks();

        List<Epic> epics = new ArrayList<>(manager.getAllEpics());
        List<Subtask> subtasks = new ArrayList<>(manager.getAllSubtasks());
        List<Task> tasks = new ArrayList<>(manager.getAllTasks());
        List<Task> history = new ArrayList<>(manager.getHistory());

        assertEquals(1, tasks.size());
        assertEquals(2, subtasks.size());
        assertEquals(1, epics.size());
        assertEquals(0, history.size());

        createManagerFromFile();

        List<Epic> epicsFromFile = new ArrayList<>(managerFromFile.getAllEpics());
        List<Subtask> subtasksFromFile = new ArrayList<>(managerFromFile.getAllSubtasks());
        List<Task> tasksFromFile = new ArrayList<>(managerFromFile.getAllTasks());
        List<Task> historyFromFile = new ArrayList<>(managerFromFile.getHistory());

        assertEquals(epicsFromFile.size(), epics.size(),
                "Размер загруженного списка эпиков не равен размеру сохраненного списка");
        assertEquals(subtasksFromFile.size(), subtasks.size(),
                "Размер загруженного списка подзадач не равен размеру сохраненного списка");
        assertEquals(tasksFromFile.size(), tasks.size(),
                "Размер загруженного списка задач не равен размеру сохраненного списка");
        assertEquals(historyFromFile.size(), history.size(),
                "Размер загруженного списка истории не равен размеру сохраненного списка");
    }

}