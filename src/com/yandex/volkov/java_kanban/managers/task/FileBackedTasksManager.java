package com.yandex.volkov.java_kanban.managers.task;

import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    final String TITLE = "id,type,title,descriptions,status,epic\n";

    public FileBackedTasksManager loadedFromFileTasksManager() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(historyManager, file);
        fileBackedTasksManager.loadFromFile();
        return fileBackedTasksManager;
    }

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
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

            for (Task task : getAllTask()) {
                writer.write(toString(task) + null + "\n");
            }

            for (Epic epic : getAllEpic()) {
                writer.write(toString(epic)  + null + "\n");
            }

            for (Subtask subtask : getAllSubtask()) {
                writer.write(toString(subtask) + "\n");
            }

            writer.write("\n");
            writer.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Задачи не сохранены", e);
        }
    }

    private TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;
    }

    private String toString(Task task) {
        String[] toJoin = {
                Integer.toString(task.getId()),
                getType(task).toString(),
                task.getTitle(),
                task.getDescriptions(),
                task.getStatus().toString(),
                getEpicsId(task)
        };
        return String.join(",", toJoin);
    }

    private Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        String type = split[1];
        String title = split[2];
        String descriptions = split[3];
        Status status = Status.valueOf(split[4].toUpperCase());
        Integer epicId = status.equals("SUBTASK") ? Integer.parseInt(split[5]) : null;


        if (status.equals("EPIC")) {
            Epic epic = new Epic(title, descriptions, status);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        } else if (status.equals("SUBTASK")) {
            Subtask subtask = new Subtask(title, descriptions, status, epicId);
            subtask.setId(id);
            return subtask;
        } else {
            Task task = new Task(title, descriptions, status);
            task.setId(id);
            return task;
        }
    }

    private String getEpicsId(Task task) {
        if (task instanceof Subtask) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    public void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String line = br.readLine();
            while (br.ready()) {
                line = br.readLine();
                if (line.equals("")) {
                    break;
                }

                Task task = fromString(line);

                if (task instanceof Epic epic) {
                    addNewEpic(epic);
                } else if (task instanceof Subtask subtask) {
                    addNewSubtask(subtask);
                } else {
                    addNewTask(task);
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

    static List<Integer> historyFromString(String value) {
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

    static String historyToString(HistoryManager manager) {
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
        save();
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(int id) {
        save();
        return super.getEpic(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        save();
        return super.getSubtask(id);
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


}


