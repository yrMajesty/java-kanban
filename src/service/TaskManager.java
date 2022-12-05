package service;


import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    Status status;
    protected int nextId = 0;
    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskMap = new HashMap<>();


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
        return nextId;
    }

    public void updateTask(Task task, int id) {
        taskMap.put(id, task);
    }

    public void updateEpic(Epic epic, int id) {
        epicMap.put(id, epic);
    }

    public void updateSubtask(Subtask subtask, int id) {
        subtaskMap.put(id, subtask);
    }

    public void getAllTask() {

        ArrayList<Task> taskList = new ArrayList<>();
        for (Integer value : taskMap.keySet()) {
            taskList.add(taskMap.get(value));
        }
        System.out.println(taskList);
    }

    public void getAllSubtask() {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer value : subtaskMap.keySet()) {
            subtaskList.add(subtaskMap.get(value));
        }
        System.out.println(subtaskList);
    }

    public void getAllEpic() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer value : epicMap.keySet()) {
            epicList.add(epicMap.get(value));
        }
        System.out.println(epicList);
    }

    public void clearTask() { taskMap.clear(); }

    public void cleareEpic() { epicMap.clear(); }

    public void clearSubtask(int id) { subtaskMap.clear(); }

    public void deleteTask(int id) { taskMap.remove(id); }

    public void deleteEpic(int id) { epicMap.remove(id); }

    public void deleteSubtask(int id) { subtaskMap.remove(id); }

    public void updateEpicStatus(int epicId) {    }

    public void getAllSubtaskInEpic(int epicId) {    }
}
