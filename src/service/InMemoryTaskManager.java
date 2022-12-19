package service;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 0;
    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
   InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


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
    public Epic getEpic(int epicId) {
        historyManager.add(epicMap.get(epicId));
        return epicMap.get(epicId);
    }
    @Override
    public Subtask getSubtask(int subId) {
        historyManager.add(subtaskMap.get(subId));
        return subtaskMap.get(subId);
    }
    @Override
    public int addNewTask(Task task) {
        task.setId(nextId++);
        taskMap.put(task.getId(), task);
        return nextId;
    }
    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(nextId++);
        epic.setStatus(Status.NEW);
        epicMap.put(epic.getId(), epic);
        return nextId;
    }
    @Override
    public int addNewSubtask(Subtask subtask, int epicId) {
        subtask.setId(nextId++);
        subtaskMap.put(subtask.getId(), subtask);
        epicMap.get(epicId).setSubtaskId(subtask.getId());
        updateEpicStatus(epicId);
        return nextId;

    }
    @Override
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }
    @Override
    public void updateEpic(Epic epic) {

        epicMap.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        updateEpicStatus(subtask.getEpicId());
    }
    @Override
    public ArrayList<Task> getAllTask() {

        ArrayList<Task> taskList = new ArrayList(taskMap.values());
        return taskList;
    }
    @Override
    public ArrayList<Subtask> getAllSubtask() {
        ArrayList<Subtask> subtaskList = new ArrayList(subtaskMap.values());
        return subtaskList;
    }
    @Override
    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> epicList = new ArrayList(epicMap.values());
        return epicList;
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
    public void clearSubtask(int subId) {
        subtaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(subtaskMap.get(subId).getEpicId());
        }
    }
    @Override
    public void deleteTask(int id) {
        taskMap.remove(id);
    }
    @Override
    public void deleteEpic(int epicId) {
        for (Integer subId : epicMap.get(epicId).getSubtaskId()) {
            subtaskMap.remove(subId);
        }
        epicMap.remove(epicId);
        updateEpicStatus(epicId);
    }
    @Override
    public void deleteSubtask(int subId) {
        int epicId = subtaskMap.get(subId).getEpicId();
        epicMap.get(subtaskMap.get(subId).getEpicId()).getSubtaskId().remove((Integer) subId);
        subtaskMap.remove(subId);
        updateEpicStatus(epicId);
    }
    @Override
    public ArrayList<Subtask> getAllSubtaskInEpic(int epicId) {

        ArrayList<Integer> subtaskIds = epicMap.get(epicId).getSubtaskId();
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtaskMap.get(subtaskId);
            subtasks.add(subtask);
        }
        return subtasks;

    }

    @Override
    public void updateEpicStatus(int epicId) {
        int statusNew = 0;
        int statusDone = 0;
        Status value;
        if (epicMap.get(epicId).getSubtaskId().isEmpty()) {
            epicMap.get(epicId).setStatus(Status.NEW);
        } else {
            for (int sub : epicMap.get(epicId).getSubtaskId()) {
                value = subtaskMap.get(sub).getStatus();
                if (value == Status.NEW) {
                    statusNew++;
                }
                if (value == Status.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == epicMap.get(epicId).getSubtaskId().size()) {
                epicMap.get(epicId).setStatus(Status.NEW);
            } else if (statusDone == epicMap.get(epicId).getSubtaskId().size()) {
                epicMap.get(epicId).setStatus(Status.DONE);
            } else {
                epicMap.get(epicId).setStatus(Status.IN_PROGRESS);
            }
        }
    }


}





