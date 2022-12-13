package service;


import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {


    private int nextId = 0;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();


    public Task getTask(int id) {

        return taskMap.get(id);
    }

    public Epic getEpic(int epicId) {

        return epicMap.get(epicId);
    }

    public Subtask getSubtask(int subId) {

        return subtaskMap.get(subId);
    }

    public int addNewTask(Task task) {
        task.setId(nextId++);
        taskMap.put(task.getId(), task);
        return nextId;
    }

    public int addNewEpic(Epic epic) {
        epic.setId(nextId++);
        epic.setStatus(Status.NEW);
        epicMap.put(epic.getId(), epic);
        return nextId;
    }

    public int addNewSubtask(Subtask subtask, int epicId) {
        subtask.setId(nextId++);
        subtaskMap.put(subtask.getId(), subtask);
        epicMap.get(epicId).setSubtaskId(subtask.getId());
        updateEpicStatus(epicId);
        return nextId;

    }

    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {

        epicMap.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    public void updateSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        updateEpicStatus(subtask.getEpicId());
    }

    public ArrayList<Task> getAllTask() {

        ArrayList<Task> taskList = new ArrayList(taskMap.values());
        return taskList;
    }

    public ArrayList<Subtask> getAllSubtask() {
        ArrayList<Subtask> subtaskList = new ArrayList(subtaskMap.values());
        return subtaskList;
    }

    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> epicList = new ArrayList(epicMap.values());
        return epicList;
    }

    public void clearTask() {
        taskMap.clear();
    }

    public void clearEpic() {
        subtaskMap.clear();
        epicMap.clear();
    }

    public void clearSubtask(int subId) {
        subtaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(subtaskMap.get(subId).getEpicId());
        }
    }

    public void deleteTask(int id) {
        taskMap.remove(id);
    }

    public void deleteEpic(int epicId) {
        for (Integer subId : epicMap.get(epicId).getSubtaskId()) {
            subtaskMap.remove(subId);
        }
        epicMap.remove(epicId);
        updateEpicStatus(epicId);
    }

    public void deleteSubtask(int subId) {
        int epicId = subtaskMap.get(subId).getEpicId();
        epicMap.get(subtaskMap.get(subId).getEpicId()).getSubtaskId().remove((Integer) subId);
        subtaskMap.remove(subId);
        updateEpicStatus(epicId);
    }

    public ArrayList<Subtask> getAllSubtaskInEpic(int epicId) {

        ArrayList<Integer> subtaskIds = epicMap.get(epicId).getSubtaskId();
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtaskMap.get(subtaskId);
            subtasks.add(subtask);
        }
        return subtasks;

    }


    private void updateEpicStatus(int epicId) {
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





