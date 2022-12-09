package service;


import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {


    protected int nextId = 0;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();


    public Task getTask(int id) {

        return taskMap.get(id);
    }

    public Epic getEpic(int id) {

        return epicMap.get(id);
    }

    public Subtask getSubtask(int id) {

        return subtaskMap.get(id);
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

    public void updateTask(Task task, int id) { //удалить int id и добавить вмесо id task

        taskMap.put(id, task);
    }

    public void updateEpic(Epic epic, int id) {

        epicMap.put(id, epic);
        updateEpicStatus(id);
    }

    public void updateSubtask(Subtask subtask, int id) {

        subtaskMap.put(id, subtask);
    }

    public ArrayList<Task> getAllTask() {

        ArrayList<Task> taskList = new ArrayList<>();
        for (Integer value : taskMap.keySet()) {
            taskList.add(taskMap.get(value));
        }
        return taskList;
    }

    public ArrayList<Subtask> getAllSubtask() {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer value : subtaskMap.keySet()) {
            subtaskList.add(subtaskMap.get(value));
        }
        return subtaskList;
    }

    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer value : epicMap.keySet()) {
            epicList.add(epicMap.get(value));
        }
        return epicList;
    }

    public void clearTask() {
        taskMap.clear();
    }

    public void clearEpic() {
        epicMap.clear();
    }

    public void clearSubtask(int id) {
        subtaskMap.clear();
    }

    public void deleteTask(int id) {
        taskMap.remove(id);
    }

    public void deleteEpic(int id) {
        epicMap.remove(id);
        for (Integer subId : subtaskMap.keySet()) {
            if (id == subtaskMap.get(subId).getEpicId()) {
                subtaskMap.remove(subId);
                subtaskMap.clear();
            }
        }
        updateEpicStatus(id);
    }

    public void deleteSubtask(int subId, int epicId) {
        for (Integer subtaskId : subtaskMap.keySet()) {
            if (epicId == subtaskMap.get(subtaskId).getEpicId()) {
                subtaskMap.remove(subtaskId);
            }
        }
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





