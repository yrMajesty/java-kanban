package com.yandex.volkov.java_kanban;
import com.yandex.volkov.java_kanban.service.history.InMemoryHistoryManager;
import com.yandex.volkov.java_kanban.service.history.InMemoryTaskManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.util.List;


public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task #1", "Task1 description", Status.NEW);
        Task task2 = new Task("Task #2", "Task2 description", Status.IN_PROGRESS);

        Epic epic1 = new Epic(1, "Epic #1", "Epic1 description", Status.NEW);
        Epic epic2 = new Epic(2, "Epic #2", "Epic2 description", Status.IN_PROGRESS);

        Subtask subtask1 = new Subtask(1, "Subtask #1-1", "Subtask1 description", Status.NEW);
        Subtask subtask2 = new Subtask(2, "Subtask #1-2", "Subtask2 description", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask(2, "Subtask #2-2", "Subtask2 description", Status.DONE);
        Subtask subtask4 = new Subtask(2, "Subtask #2-1", "Subtask2 description", Status.DONE);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubtask(subtask1, epic1.getId());
        taskManager.addNewSubtask(subtask2, epic1.getId());
        taskManager.addNewSubtask(subtask3, epic2.getId());
        taskManager.addNewSubtask(subtask4, epic2.getId());

        taskManager.getEpic(2);
        taskManager.getSubtask(2);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(1);


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

        System.out.println("История просмотренных задач");
        List<Task> history = taskManager.getHistory();
        System.out.println(history);


    }

}
