package com.yandex.volkov.java_kanban.managers.task;

import com.yandex.volkov.java_kanban.converter.TaskConverter;
import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {
        Path path = Path.of("data.csv");
        File file = new File(String.valueOf(path));

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Manager.getDefaultHistory(), file);
        System.out.println("История до:");
        System.out.println(fileBackedTasksManager.getHistory() + "\n");
        Task task1 = new Task("Task #1", "#1 Тут могла быть ваша реклама", Status.NEW); //1
        Task task2 = new Task("Task #2", "#2 Тут могла быть ваша реклама", Status.IN_PROGRESS); //2

        Epic epic1 = new Epic("Epic #1", "Epic1 description", Status.NEW); //3
        Epic epic2 = new Epic("Epic #2", "Epic2 description", Status.IN_PROGRESS); //4

        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", Status.NEW, 3); //5
        Subtask subtask2 = new Subtask("Subtask #1-2", "Subtask2 description", Status.IN_PROGRESS, 3); //6
        Subtask subtask3 = new Subtask("Subtask #1-3", "Subtask3 description", Status.DONE, 3); //7
        Subtask subtask4 = new Subtask("Subtask #2-1", "Subtask1 description", Status.DONE, 4); //8


        fileBackedTasksManager.addNewTask(task1);
        fileBackedTasksManager.addNewTask(task2);
        fileBackedTasksManager.addNewEpic(epic1);
        fileBackedTasksManager.addNewEpic(epic2);
        fileBackedTasksManager.addNewSubtask(subtask1);
        fileBackedTasksManager.addNewSubtask(subtask2);
        fileBackedTasksManager.addNewSubtask(subtask3);
        fileBackedTasksManager.addNewSubtask(subtask4);


        fileBackedTasksManager.getSubtask(6);
        fileBackedTasksManager.getEpic(4);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getTask(2);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getEpic(4);
        fileBackedTasksManager.getSubtask(6);


        FileBackedTasksManager fileBackedTasksManager2 = new FileBackedTasksManager(Manager.getDefaultHistory(), file);
        fileBackedTasksManager2.loadFromFile();

        System.out.println("Задачи");
        System.out.println(fileBackedTasksManager2.getAllTasks());
        System.out.println("Эпики");
        System.out.println(fileBackedTasksManager2.getAllEpics());
        System.out.println("Подзадачи");
        System.out.println(fileBackedTasksManager2.getAllSubtasks());
        System.out.println("История");
        System.out.println(fileBackedTasksManager2.getHistory());

    }

    private final File file;
    private static final String TITLE = "id,type,title,descriptions,status,epic\n";

    static public FileBackedTasksManager loadedFromFileTasksManager(HistoryManager historyManager, File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(historyManager, file);
        fileBackedTasksManager.loadFromFile();
        return fileBackedTasksManager;
    }

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
            writer.write(TaskConverter.historyToString(getHistoryManager()));
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


