package com.yandex.volkov.java_kanban.managers.task;


import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask);

    void clearTask();

    void clearEpic();

    void clearSubtask(int subId);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtaskById(Integer id);

    List<Subtask> getAllSubtaskInEpic(int id);

}





