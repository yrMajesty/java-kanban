
import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.task.FileBackedTasksManager;
import com.yandex.volkov.java_kanban.managers.task.TaskManager;
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
    void createManager() {
        manager = new FileBackedTasksManager(Manager.getDefaultHistory(), FILE_TEST);
    }

    @BeforeEach
    void createFile() {
        if (Files.notExists(Path.of(FILE_TEST.getPath()))) {
            try {
                Files.createFile(Path.of(FILE_TEST.getPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AfterEach
    void deleteFile() {
        try {
            Files.delete(Path.of(FILE_TEST.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void createManagerFromFile(){
        managerFromFile = FileBackedTasksManager.loadedFromFileTasksManager(Manager.getDefaultHistory(), FILE_TEST);
        managerFromFile.loadFromFile();
    }

    void addTasks(){
        manager.addNewTask(task);
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
    }

    @Test
    void shouldReturnEmptyListsWithAllTypeTasksWhenCreateManagerFromEmptyFile() {
        createManagerFromFile();

        List<Epic> epics = new ArrayList<>(managerFromFile.getAllEpics());
        List<Task> tasks = new ArrayList<>(managerFromFile.getAllTasks());
        List<Subtask> subtasks = new ArrayList<>(managerFromFile.getAllSubtasks());

        assertTrue(epics.isEmpty(), "При чтении из  файла список эпиков не пустой");
        assertTrue(subtasks.isEmpty(), "При чтении из  файла список подзадач не пустой");
        assertTrue(tasks.isEmpty(), "При чтении из  файла список задач не пустой");

    }

    @Test
    void shouldReturnManagerFromFileWithOneEpicWithoutSubtasks() {
        List<Epic> epics = new ArrayList<>(manager.getAllEpics());
        List<Subtask> subtasks = new ArrayList<>(manager.getAllSubtasks());

        assertTrue(epics.isEmpty(), "Список эпиков пустой");
        assertTrue(subtasks.isEmpty(), "Список подзадач пустой");

        createManagerFromFile();

        List<Epic> epicsFromFile = new ArrayList<>(managerFromFile.getAllEpics());
        List<Subtask> subtasksFromFile = new ArrayList<>(managerFromFile.getAllSubtasks());

        assertTrue(epicsFromFile.isEmpty(), "При чтении из  файла список эпиков непустой");
        assertTrue(subtasksFromFile.isEmpty(), "При чтении из  файла список подзадач не пустой");
    }

    @Test
    void shouldReturnManagerFromFileWithNotEmptyHistory() {
        addTasks();

        manager.getTask(task.getId());

        List<Epic> epics = new ArrayList<>(manager.getAllEpics());
        List<Subtask> subtasks = new ArrayList<>(manager.getAllSubtasks());
        List<Task> tasks = new ArrayList<>(manager.getAllTasks());
        List<Task> history = new ArrayList<>(manager.getHistory());

        assertFalse(epics.isEmpty(), "Список эпиков пустой");
        assertFalse(subtasks.isEmpty(), "Список подзадач пустой");
        assertFalse(tasks.isEmpty(), "Список задач пустой");
        assertFalse(history.isEmpty(), "Список истории пустой");

        assertEquals(1, tasks.size());
        assertEquals(2, subtasks.size());
        assertEquals(1, epics.size());
        assertEquals(1, history.size());

        createManagerFromFile();

        List<Epic> epicsFromFile = new ArrayList<>(managerFromFile.getAllEpics());
        List<Subtask> subtasksFromFile = new ArrayList<>(managerFromFile.getAllSubtasks());
        List<Task> tasksFromFile = new ArrayList<>(managerFromFile.getAllTasks());
        List<Task> historyFromFile = new ArrayList<>(managerFromFile.getHistory());

        assertFalse(epicsFromFile.isEmpty(),
                "При чтении из файла с эпиками список эпиков пустой");
        assertFalse(subtasksFromFile.isEmpty(),
                "При чтении из файла с подзадачами список подзадач пустой");
        assertFalse(tasksFromFile.isEmpty(),
                "При чтении из файла с задачами список подзадач пустой");
        assertFalse(historyFromFile.isEmpty(),
                "При чтении из  файла c историей, восстановленная история пустая");
    }

    @Test
    void shouldReturnManagerFromFileWithEmptyHistory() {
        addTasks();

        List<Epic> epics = new ArrayList<>(manager.getAllEpics());
        List<Subtask> subtasks = new ArrayList<>(manager.getAllSubtasks());
        List<Task> tasks = new ArrayList<>(manager.getAllTasks());
        List<Task> history = new ArrayList<>(manager.getHistory());

        assertFalse(epics.isEmpty(), "Список эпиков пустой");
        assertFalse(subtasks.isEmpty(), "Список подзадач пустой");
        assertFalse(tasks.isEmpty(), "Список задач пустой");
        assertTrue(history.isEmpty(), "Список истории пустой");

        assertEquals(1, tasks.size());
        assertEquals(2, subtasks.size());
        assertEquals(1, epics.size());
        assertEquals(0, history.size());

        createManagerFromFile();

        List<Epic> epicsFromFile = new ArrayList<>(managerFromFile.getAllEpics());
        List<Subtask> subtasksFromFile = new ArrayList<>(managerFromFile.getAllSubtasks());
        List<Task> tasksFromFile = new ArrayList<>(managerFromFile.getAllTasks());
        List<Task> historyFromFile = new ArrayList<>(managerFromFile.getHistory());

        assertFalse(epicsFromFile.isEmpty(),
                "При чтении из файла с эпиками список эпиков пустой");
        assertFalse(subtasksFromFile.isEmpty(),
                "При чтении из файла с подзадачами список подзадач пустой");
        assertFalse(tasksFromFile.isEmpty(),
                "При чтении из файла с задачами список подзадач пустой");
        assertTrue(historyFromFile.isEmpty(),
                "При чтении из файла с пустой историей, " +
                        "история восстановленного менеджера не пустая");
    }

}