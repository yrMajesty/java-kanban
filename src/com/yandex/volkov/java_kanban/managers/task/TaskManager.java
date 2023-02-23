package com.yandex.volkov.java_kanban.managers.task;

import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubtask();

    List<Task> getHistory();

    void remove(int id);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Task> getAllTasks();

    Task addNewTask(Task task);

    Epic addNewEpic(Epic epic);

    Subtask addNewSubtask(Subtask subtask);

    void clearTask();

    void clearEpic();

    void clearSubtask(int subId);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtaskById(Integer id);

    List<Subtask> getAllSubtaskInEpic(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);
    List<Task> getPrioritizedTasks();

}





