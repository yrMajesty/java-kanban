package com.yandex.volkov.java_kanban.managers.task;

import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final Map<Integer, Subtask> subtaskMap = new HashMap<>();
    HistoryManager historyManager = Manager.getDefaultHistory();


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public int addNewTask(Task task) {
        task.setId(nextId++);
        taskMap.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(nextId++);
        epicMap.put(epic.getId(), epic);
        for (Subtask subtask : subtaskMap.values()) {
            if (epic.getId() == subtask.getEpicId()) {
                epic.getSubtaskId().add(subtask.getId());
            }
        }
        return epic.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {

        subtask.setId(nextId++);
        subtaskMap.put(subtask.getId(), subtask);
        for (Epic epic : epicMap.values()) {
            if (epic.getId() == subtask.getEpicId()) {
                epic.getSubtaskId().add(subtask.getId());
            }
        }
        if (epicMap.containsKey(subtask.getEpicId())) {
            updateEpicStatus(epicMap.get(subtask.getEpicId()));

        }
        return subtask.getId();
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





