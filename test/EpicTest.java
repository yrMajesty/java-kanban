import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.managers.task.InMemoryTaskManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EpicTest {
    InMemoryTaskManager manager;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    @Test
    public void createManagerAndEpic() {
        HistoryManager historyManager = Manager.getDefaultHistory();
        manager = new InMemoryTaskManager(historyManager);
        epic = new Epic("Title Epic", "Descr Epic", Status.NEW,
                LocalDateTime.of(2023, 2, 23, 8, 0, 0), 60);
        manager.addNewEpic(epic);
    }

    @Test
    public void shouldEmptyListOfSubtask(){
        List<Subtask> listSubtasks = manager.getAllSubtaskInEpic(epic.getId());
        assertTrue(listSubtasks.isEmpty(), "List of all subtask is not empty");
    }

    @Test
    public void shouldStatusNewOfEpicWhichWithSubtaskWithStatusNew(){
        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 11, 0, 0), 60);
        manager.addNewSubtask(subtask1);
        Status statusEpic = manager.getEpic(epic.getId()).getStatus();
        assertEquals(subtask1.getStatus(), statusEpic, "Status of new epic is not NEW");
    }

    @Test
    public void shouldStatusInProgressOfEpicWhichWithSubtasksWithStatusNewAndDone(){
        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 11, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.DONE, epic.getId(),
                LocalDateTime.of(2023, 1, 5, 11, 0, 0), 60);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(subtask1.getEpicId()).getStatus(),
                "Status of epic with one task(status DONE) and one task (status NEW) is not IN_PROGRESS");
    }

    @Test
    public void shouldStatusInProgressOfEpicWhichWithSubtasksWithStatusInProgress(){
        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 11, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.DONE, epic.getId(),
                LocalDateTime.of(2023, 1, 5, 11, 0, 0), 60);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(subtask1.getEpicId()).getStatus(),
                "Status of epic with two task(status IN_PROGRESS) is not IN_PROGRESS");
    }

    @Test
    public void shouldStatusDoneOfEpicWhichWithSubtasksWithStatusDone(){
        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.DONE, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 11, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.DONE, epic.getId(),
                LocalDateTime.of(2023, 1, 4, 11, 0, 0), 60);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        assertEquals(Status.DONE, manager.getEpic(subtask1.getEpicId()).getStatus(),
                "Status of epic with two task(status DONE) is not DONE");
    }

    @Test
    void shouldReturnTrueDurationEpicWithTwoSubtasks() {
        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.DONE, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 11, 0, 0), 60);
        manager.addNewSubtask(subtask1);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.DONE, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 13, 0, 0), 60);
        manager.addNewSubtask(subtask2);
        assertEquals(epic.getDuration(),
                ChronoUnit.MINUTES.between(subtask1.getStartTime(), subtask2.getEndTime()),
                "Duration in minutes of epic is not equals sum of duration of subtasks and time between subtasks in epic");
    }

}