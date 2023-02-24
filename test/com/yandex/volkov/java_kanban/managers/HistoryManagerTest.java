package com.yandex.volkov.java_kanban.managers;

import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    InMemoryTaskManager manager;
    Task task;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void initManagerAndTasks() {
        HistoryManager historyManager = Manager.getDefaultHistory();
        manager = new InMemoryTaskManager(historyManager);

        task = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 2, 1, 10, 0, 0), 10);
        manager.addNewTask(task);

        epic = new Epic("Title Epic", "Descr Epic", Status.NEW,
                LocalDateTime.of(2023, 2, 23, 8, 0, 0), 60);
        manager.addNewEpic(epic);

        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 11, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.DONE, epic.getId(),
                LocalDateTime.of(2023, 1, 5, 11, 0, 0), 60);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
    }

    @Test
    void getHistory_returnEmptyHistory() {
        assertTrue(manager.getHistory().isEmpty(), "История просмотров не пустая");
    }

    @Test
    void getHistory_returnHistoryContainsViewedTask_viewedOneTask() {
        manager.getTask(task.getId());
        assertTrue(manager.getHistory().contains(task), "История просмотров не содержит просмотренную задачу");
    }

    @Test
    void getHistory_returnHistorySize3_viewThreeDifferentTasks() {
        manager.getTask(task.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        assertEquals(3, manager.getHistory().size(),
                "История просмотров не равна 3, после просмотра трех разных задач");
    }

    @Test
    void getHistory_returnHistorySize2_viewThreeTasksTwoOfWhichTheSame() {
        manager.getTask(task.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());

        manager.getHistoryManager().remove(task.getId());
        assertEquals(2, manager.getHistory().size(),
                "История просмотров не равна 2, после удаления первой задачи");
        assertFalse(manager.getHistory().contains(task),
                "История просмотров содержит удаленную задачу");
    }

    @Test
    void getHistory_returnHistorySize2_viewThreeDifferentTasksAndRemovedLast() {
        manager.getTask(task.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());

        manager.getHistoryManager().remove(subtask2.getId());
        assertEquals(2, manager.getHistory().size(),
                "История просмотров не равна 2, после удаления последней просмотренной задачи");
        assertFalse(manager.getHistory().contains(subtask2),
                "История просмотров содержит удаленную подзадачу");
    }

    @Test
    void getHistory_returnHistorySize1AndContainsViewedEpicAndDoesNotContainsEpicSubtasks_viewedEpic() {
        manager.getEpic(epic.getId());
        assertEquals(1, manager.getHistory().size(),
                "История просмотров не равна 1, после просмотра одного эпика с подзадачами");
        for (Integer idSubtask : epic.getSubtaskId()) {
            Subtask subtask = manager.getSubtaskMap().get(idSubtask);
            assertFalse(manager.getHistory().contains(subtask),
                    "История просмотров содержит подзадачу просмотренного эпика");
        }
    }

    @Test
    void getHistory_returnHistorySize1_viewOneEpicTwice() {
        manager.getEpic(epic.getId());
        manager.getEpic(epic.getId());
        assertEquals(1, manager.getHistory().size(),
                "История просмотров не равна 1, после дважды просмотренного одного эпика с подзадачами");
    }

    @Test
    void getHistory_returnEmptyListHistory_noViews() {
        assertTrue(manager.getHistory().isEmpty(), "История просмотров не пустая при отсутствии просмотров");
    }
}