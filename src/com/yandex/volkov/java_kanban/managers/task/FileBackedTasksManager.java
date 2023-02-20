package com.yandex.volkov.java_kanban.managers.task;

import com.yandex.volkov.java_kanban.converter.TaskConverter;
import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {
        Path path = Path.of("data.csv");
        File file = new File(String.valueOf(path));

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Manager.getDefaultHistory(), file);

        fileBackedTasksManager.loadFromFile();

        System.out.println("Задачи");
        System.out.println(fileBackedTasksManager.getAllTasks());
        System.out.println("Эпики");
        System.out.println(fileBackedTasksManager.getAllEpics());
        System.out.println("Подзадачи");
        System.out.println(fileBackedTasksManager.getAllSubtasks());
        System.out.println("История");
        System.out.println(fileBackedTasksManager.getHistory());

    }

    private final File file;
    private static final String TITLE = "id,type,title,descriptions,status,epic\n";

    private FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    private void save() {
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

            for (Task task : getAllTask()) {
                writer.write(TaskConverter.toString(task));
            }

            for (Epic epic : getAllEpic()) {
                writer.write(TaskConverter.toString(epic));
            }

            for (Subtask subtask : getAllSubtask()) {
                writer.write(TaskConverter.toString(subtask));
            }

            writer.write("\n");
            writer.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Задачи не сохранены", e);
        }
    }

    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String line = br.readLine();
            while (br.ready()) {
                line = br.readLine();
                if (line.equals("")) {
                    break;
                }

                TaskType type = getTypeFromLine(line);
                if (type == TaskType.EPIC) {
                    Epic epic = TaskConverter.getEpicFromString(line);
                    epicMap.put(epic.getId(), epic);
                } else if (type == TaskType.SUBTASK) {
                    Subtask subtask = TaskConverter.getSubTaskFromString(line);
                    subtaskMap.put(subtask.getId(), subtask);
                } else {
                    Task task = TaskConverter.getTaskFromString(line);
                    taskMap.put(task.getId(), task);
                }
            }

            String historyLine = br.readLine();
            for (int id : historyFromString(historyLine)) {
                addToHistory(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
    }

    private TaskType getTypeFromLine(String line) {
        String[] split = line.split(",");
        return TaskType.valueOf(split[1].toUpperCase());
    }

    private static List<Integer> historyFromString(String value) {
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

    private static String historyToString(HistoryManager manager) {
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
    public void clearSubtask(int id) {
        super.clearSubtask(id);
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
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public List<Subtask> getAllSubtaskInEpic(int id) {
        save();
        return super.getAllSubtaskInEpic(id);

    }
    @Override
    public Task addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
        return subtask;
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


}


