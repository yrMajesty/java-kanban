package com.yandex.volkov.java_kanban.managers.task;

import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int nextId = 1;
    protected Map<Integer, Task> taskMap = new HashMap<>();
    protected Map<Integer, Epic> epicMap = new HashMap<>();
    protected Map<Integer, Subtask> subtaskMap = new HashMap<>();
    HistoryManager historyManager = Manager.getDefaultHistory();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Epic> getAllEpic() {
        if (epicMap.size() == 0) {
            System.out.println("Данные с EPIC отсутсвуют");
            return null;
        }
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Task> getAllTask() {
        if (taskMap.size() == 0) {
            System.out.println("Данные с TASK отсутсвуют");
            return null;
        }
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
        if (subtaskMap.size() == 0) {
            System.out.println("Данные с SUBTASK отсутсвуют");
            return Collections.emptyList();
        }
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void addToHistory(int id) {
        if (epicMap.containsKey(id)) {
            historyManager.add(epicMap.get(id));
        } else if (subtaskMap.containsKey(id)) {
            historyManager.add(subtaskMap.get(id));
        } else if (taskMap.containsKey(id)) {
            historyManager.add(taskMap.get(id));
        }
    }

    @Override
    public void remove(int id) {
        historyManager.remove(id);
    }

    @Override
    public Task getTask(int id) {
        Task task = taskMap.get(id);
        if (task != null) {
            historyManager.add(taskMap.get(id));
        }
        return taskMap.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            historyManager.add(epicMap.get(id));
        }
        return epicMap.get(id);
    }

    @Override

    public Subtask getSubtask(int id) {
        Subtask subtask = subtaskMap.get(id);
        if (subtask != null) {
            historyManager.add(subtaskMap.get(id));
        }
        return subtaskMap.get(id);
    }

    @Override
    public List<Epic> getAllEpics() {
        if (epicMap.size() == 0) {
            System.out.println("Данные с EPIC отсутсвуют");
            return Collections.emptyList();
        }
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        if (subtaskMap.size() == 0) {
            System.out.println("Данные с SUBTASK отсутсвуют");
            return Collections.emptyList();
        }
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public List<Task> getAllTasks() {
        if (taskMap.size() == 0) {
            System.out.println("Данные с TASK отсутсвуют");
            return Collections.emptyList();
        }
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public Task addNewTask(Task task) {
        if (task == null) return null;
        task.setId(nextId++);
        taskMap.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        if (epic == null) return null;
        epic.setId(nextId++);
        epicMap.put(epic.getId(), epic);
        for (Subtask subtask : subtaskMap.values()) {
            if (epic.getId() == subtask.getEpicId()) {
                epic.getSubtaskId().add(subtask.getId());
            }
        }
        return epic;
    }

    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        if (subtask == null) return null;
        subtask.setId(nextId++);
        subtaskMap.put(subtask.getId(), subtask);

        for (Epic epic : epicMap.values()) {
            if (epic != null) {
                if (epic.getId() == subtask.getEpicId()) {
                    epic.getSubtaskId().add(subtask.getId());
                }
                if (epicMap.containsKey(subtask.getEpicId())) {
                    updateEpicStatus(epicMap.get(subtask.getEpicId()));
                }
            } else {
                System.out.println("Данные с EPIC отсутсвуют");
                return null;
            }
        }
        return subtask;
    }

    @Override
    public void clearTask() {

        taskMap.clear();
    }

    @Override
    public void clearEpic() {
        subtaskMap.clear();
        epicMap.clear();
    }

    @Override
    public void clearSubtask(int id) {
        subtaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epicMap.get(id));
        }
    }

    @Override
    public void deleteTask(int id) {
        taskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (epicMap.get(id).getSubtaskId() == null) return;

        List<Integer> subEpic = epicMap.get(id).getSubtaskId();
        for (int subtaskId : subEpic) {
            historyManager.remove(subtaskId);
            subtaskMap.remove(subtaskId);
        }
        historyManager.remove(id);
        epicMap.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        int epicID = subtaskMap.get(id).getEpicId();
        epicMap.get(epicID).getSubtaskId().remove(id);
        updateEpicStatus(epicMap.get(epicID));
        historyManager.remove(subtaskMap.get(id).getId());
        subtaskMap.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtaskInEpic(int id) {

        List<Subtask> subtasksByEpic = new ArrayList<>();
        List<Integer> subList = epicMap.get(id).getSubtaskId();

        for (Integer subId : subList) {

            subtasksByEpic.add(subtaskMap.get(subId));
        }
        return subtasksByEpic;

    }

    private void updateEpicStatus(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        Status value;
        if (epicMap.get(epic.getId()).getSubtaskId().isEmpty()) {
            epicMap.get(epic.getId()).setStatus(Status.NEW);
        } else {
            for (int sub : epicMap.get(epic.getId()).getSubtaskId()) {
                value = subtaskMap.get(sub).getStatus();
                if (value == Status.NEW) {
                    statusNew++;
                }
                if (value == Status.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == epicMap.get(epic.getId()).getSubtaskId().size()) {
                epicMap.get(epic.getId()).setStatus(Status.NEW);
            } else if (statusDone == epicMap.get(epic.getId()).getSubtaskId().size()) {
                epicMap.get(epic.getId()).setStatus(Status.DONE);
            } else {
                epicMap.get(epic.getId()).setStatus(Status.IN_PROGRESS);
            }
        }
    }


}





