package com.yandex.volkov.java_kanban;

import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.task.TaskManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;


public class Main {

    public static void main(String[] args) {

    /*    TaskManager taskManager = Manager.getDefault(Manager.getDefaultHistory());


        Task task1 = new Task("Task #1", "#1 Тут могла быть ваша реклама", Status.NEW);
        Task task2 = new Task("Task #2", "#2 Тут могла быть ваша реклама", Status.IN_PROGRESS);

        Epic epic1 = new Epic("Epic #1", "Epic1 description", Status.NEW);
        Epic epic2 = new Epic("Epic #2", "Epic2 description", Status.IN_PROGRESS);

        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", Status.NEW, 3);
        Subtask subtask2 = new Subtask("Subtask #1-2", "Subtask2 description", Status.IN_PROGRESS, 3);
        Subtask subtask3 = new Subtask("Subtask #1-3", "Subtask3 description", Status.DONE, 3);
        Subtask subtask4 = new Subtask("Subtask #2-1", "Subtask1 description", Status.DONE, 4);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);
        taskManager.addNewSubtask(subtask4);

        taskManager.getSubtask(5);
        taskManager.getEpic(3);
        taskManager.getEpic(4);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getEpic(4);
        taskManager.getSubtask(8);


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

        taskManager.deleteTask(1);
        System.out.println("history просмотренных задач после удаления");
        System.out.println(taskManager.getHistory());


        taskManager.deleteEpic(3);
        System.out.println("history просмотренных задач после удаления эпиков");
        System.out.println(taskManager.getHistory());

*/
    }

}
