package com.yandex.volkov.java_kanban;

import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.task.FileBackedTasksManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.io.File;
import java.nio.file.Path;


public class Main {

    public static void main(String[] args) {
       /* Path path = Path.of("data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager taskManager = new FileBackedTasksManager(Manager.getDefaultHistory(), file);

        Task task1 = new Task("Task #1", "#1 Тут могла быть ваша реклама", Status.NEW);
        Task task2 = new Task("Task #2", "#2 Тут могла быть ваша реклама", Status.IN_PROGRESS);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Epic1 description", Status.NEW);
        Epic epic2 = new Epic("Epic #2", "Epic2 description", Status.IN_PROGRESS);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask #1-2", "Subtask2 description", Status.IN_PROGRESS, epic1.getId());
        Subtask subtask3 = new Subtask("Subtask #1-3", "Subtask3 description", Status.DONE, epic1.getId());
        Subtask subtask4 = new Subtask("Subtask #2-1", "Subtask1 description", Status.DONE, epic2.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);
        taskManager.addNewSubtask(subtask4);

        taskManager.getSubtask(subtask1.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubtask(subtask4.getId());

        System.out.println("Таски");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println("Эпики");
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println("Сабтаски");
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
        System.out.println(subtask4);

        System.out.println("history просмотренных задач");

        System.out.println(taskManager.getHistory());

        taskManager.deleteTask(task1.getId());
        System.out.println("history просмотренных задач после удаления");
        System.out.println(taskManager.getHistory());


        taskManager.deleteEpic(epic1.getId());
        System.out.println("history просмотренных задач после удаления эпиков");
        System.out.println(taskManager.getHistory());

        taskManager.loadFromFile();

        System.out.println("Задачи");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Эпики");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Подзадачи");
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("История");
        System.out.println(taskManager.getHistory());

        */
    }

}
