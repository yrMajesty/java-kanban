
import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.managers.task.InMemoryTaskManager;
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
    void createManagerAndTasks() {
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
    void shouldReturnEmptyHistory() {
        assertTrue(manager.getHistory().isEmpty(), "История просмотров не пустая");
    }

    @Test
    void shouldReturnHistoryContainsViewedTask() {
        manager.getTask(task.getId());
        assertTrue(manager.getHistory().contains(task), "История просмотров не содержит просмотренную задачу");
    }

    @Test
    void shouldReturnHistorySize3AfterViewThreeDifferentTasks() {
        manager.getTask(task.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        assertEquals(3, manager.getHistory().size(),
                "История просмотров не равна 3, после просмотра трех разных задач");
    }


    @Test
    void shouldReturnHistorySize2AfterViewThreeDifferentTasksAndRemovedFirst() {
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
    void shouldReturnHistorySize2AfterViewThreeDifferentTasksAndRemovedOneFromMiddle() {
        manager.getTask(task.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());

        manager.getHistoryManager().remove(subtask1.getId());
        assertEquals(2, manager.getHistory().size(),
                "История просмотров не равна 2, после удаления задачи из середины");
        assertFalse(manager.getHistory().contains(subtask1),
                "История просмотров содержит удаленную подзадачу");
    }

    @Test
    void shouldReturnHistorySize2AfterViewThreeDifferentTasksAndRemovedLast() {
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
    void shouldReturnHistorySize1AndContainsViewedEpic() {
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
    void shouldReturnHistorySize1AndContainsEpicViewedTwice() {
        manager.getEpic(epic.getId());
        manager.getEpic(epic.getId());
        assertEquals(1, manager.getHistory().size(),
                "История просмотров не равна 1, после дважды просмотренного одного эпика с подзадачами");
    }

    @Test
    void shouldReturnEmptyListHistoryIfNoViews() {
        assertTrue(manager.getHistory().isEmpty(), "История просмотров не пустая при отсутсвии просмотров");
    }
}