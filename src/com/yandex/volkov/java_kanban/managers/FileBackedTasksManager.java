package com.yandex.volkov.java_kanban.managers;

import com.yandex.volkov.java_kanban.converter.TaskConverter;
import com.yandex.volkov.java_kanban.exceptions.ManagerSaveException;
import com.yandex.volkov.java_kanban.task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    private static final String TITLE = "id,type,title,descriptions,status,startTime,duration,epic\n";

    public static FileBackedTasksManager loadedFromFileTasksManager(HistoryManager historyManager, File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(historyManager, file);
        fileBackedTasksManager.loadFromFile();
        return fileBackedTasksManager;
    }

    public FileBackedTasksManager() {
        file = new File("default.csv");
    }

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public FileBackedTasksManager(String file) {
        this.file = new File(file);
        historyManager = Manager.getDefaultHistory();
    }

    public void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден");
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(TITLE);

            for (Task task : getAllTasks()) {
                writer.write(TaskConverter.toString(task));
            }

            for (Epic epic : getAllEpics()) {
                writer.write(TaskConverter.toString(epic));
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(TaskConverter.toString(subtask));
            }

            writer.write("\n");
            writer.write(TaskConverter.historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Задачи не сохранены", e);
        }
    }

    public void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String line = br.readLine();
            while (br.ready()) {
                line = br.readLine();
                if (line.equals("")) {
                    break;
                }

                TaskType type = TaskConverter.getTypeFromLine(line);
                if (type == TaskType.EPIC) {
                    Epic epic = TaskConverter.getEpicFromString(line);
                    epicMap.put(epic.getId(), epic);
                    idSet.add(epic.getId());
                } else if (type == TaskType.SUBTASK) {
                    Subtask subtask = TaskConverter.getSubTaskFromString(line);
                    subtaskMap.put(subtask.getId(), subtask);
                    idSet.add(subtask.getId());
                } else {
                    Task task = TaskConverter.getTaskFromString(line);
                    taskMap.put(task.getId(), task);
                    idSet.add(task.getId());
                }
            }

            addSubTasksToEpic();

            String historyLine = br.readLine();
            for (int id : TaskConverter.historyFromString(historyLine)) {
                addToHistory(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
    }


    private void addSubTasksToEpic() {
        for (Map.Entry<Integer, Subtask> integerSubtaskEntry : subtaskMap.entrySet()) {
            Subtask subtask = integerSubtaskEntry.getValue();
            Epic epic = epicMap.get(subtask.getEpicId());
            if (epic == null) {
                System.out.println("Не найден эпик с id =" + subtask.getEpicId());
            } else {
                epic.setSubtaskId(subtask.getId());
            }
        }
    }


    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubtask() {
        super.clearSubtask();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public List<Subtask> getAllSubtaskInEpic(int id) {
        List<Subtask> allSubtaskInEpic = super.getAllSubtaskInEpic(id);
        save();
        return allSubtaskInEpic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task addNewTask(Task task) {
        Task newTask = super.addNewTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        Epic newEpic = super.addNewEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        Subtask newSubtask = super.addNewSubtask(subtask);
        save();
        return newSubtask;
    }
}


