
import com.yandex.volkov.java_kanban.managers.task.ManagerValidateException;
import com.yandex.volkov.java_kanban.managers.task.TaskManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;

    @BeforeEach
    public void createTasks() {
        task = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 2, 1, 10, 0, 0), 10);

        epic = new Epic("Title Epic", "Descr Epic", Status.NEW,
                LocalDateTime.of(2023, 2, 23, 8, 0, 0), 60);

        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 11, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.DONE,
                LocalDateTime.of(2023, 1, 5, 11, 0, 0), 60);
    }

    @Test
    void shouldCreatedTaskWithStatusNew() {
        manager.addNewTask(task);

        assertEquals(Status.NEW, task.getStatus(), "Статус новой задачи не NEW");
    }

    @Test
    void shouldCreateEpicWithStatusNew() {
        manager.addNewEpic(epic);

        assertEquals(Status.NEW, epic.getStatus(), "Статус только что созданного эпика не NEW");
    }

    @Test
    void shouldCreatedSubtaskWithStatusNew() {
        manager.addNewSubtask(subtask1);

        assertEquals(Status.NEW, subtask1.getStatus(), "Статус только что созданной подзадачи не NEW");
    }

    @Test
    void shouldEpicWithStatusNewAfterCreateSubtask() {
        manager.addNewSubtask(subtask1);

        assertEquals(Status.NEW, epic.getStatus(),
                "После создания подзадачи со статусом NEW, статус эпика подзадачи не NEW");
    }

    @Test
    void shouldNotEmptyListTasksWithCreatedTask() {
        manager.addNewTask(task);
        List<Task> allTasks = new ArrayList<>(manager.getAllTasks());

        assertFalse(allTasks.isEmpty(),
                "Список всех задач пустой");
        assertTrue(allTasks.contains(task),
                "После создания задачи, список всех задач не содержит созданную задачу");
    }

    @Test
    void shouldNotEmptyListEpicsWithCreatedEpic() {
        manager.addNewEpic(epic);
        List<Epic> allEpics = new ArrayList<>(manager.getAllEpics());

        assertFalse(allEpics.isEmpty(),
                "Список всех эпиков пустой");
        assertTrue(allEpics.contains(epic),
                "После создания эпика, список всех эпиков не содержит созданный эпик");
    }

    @Test
    void shouldNotEmptyListSubtasksWithCreatedSubtask() {
        manager.addNewSubtask(subtask1);
        List<Subtask> allSubtasks = new ArrayList<>(manager.getAllSubtasks());

        assertFalse(allSubtasks.isEmpty(), "Список всех подзадач пустой");
        assertTrue(allSubtasks.contains(subtask1),
                "После создания подзадачи, список всех подзадач не содержит созданную подзадачу");
    }

    @Test
    void shouldNotEmptyListEpicsWithEpicCreatedSubtask() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        List<Epic> allEpics = new ArrayList<>(manager.getAllEpics());

        assertFalse(allEpics.isEmpty(), "Список всех эпиков пустой");
        assertTrue(allEpics.contains(epic),
                "После создания подзадачи, список всех эпиков не содержит эпик подзадачи");
    }

    @Test
    void shouldReturnEmptyListTasksWithoutTasks() {
        Collection<Task> allTasks = manager.getAllTasks();
        assertTrue(allTasks.isEmpty(), "Список всех задач не пустой");
    }

    @Test
    void shouldReturnEmptyListEpicsWithoutEpics() {
        Collection<Task> allTasks = manager.getAllTasks();
        assertTrue(allTasks.isEmpty(), "Список всех эпиков не пустой");
    }

    @Test
    void shouldReturnEmptyListSubtasksWithoutSubtasks() {
        Collection<Subtask> allSubtasks = manager.getAllSubtasks();
        assertTrue(allSubtasks.isEmpty(), "Список всех подзадач не пустой");
    }

    @Test
    void shouldReturnEmptyListTasksAfterDeleteAllTasks() {
        manager.addNewTask(task);
        assertFalse(manager.getAllTasks().isEmpty(), "Список всех задач пустой");

        manager.clearEpic();
        assertTrue(manager.getAllEpics().isEmpty(), "Список всех эпиков не пустой");
    }

    @Test
    void shouldReturnEmptyListEpicsAfterDeleteAllEpics() {
        manager.addNewEpic(epic);
        assertFalse(manager.getAllEpics().isEmpty(), "Список всех эпиков пустой");

        manager.clearEpic();
        assertTrue(manager.getAllEpics().isEmpty(), "Список всех эпиков не пустой");
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterDeleteAllEpics() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask2);
        assertFalse(manager.getAllSubtasks().isEmpty(), "Список всех подзадач пустой");

        manager.clearEpic();
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список всех подзадач не пустой");
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterDeleteAllSubtasks() {
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);

        assertFalse(manager.getAllSubtasks().isEmpty(), "Список всех подзадач пустой");

        manager.clearSubtask(subtask1.getId());
        manager.clearSubtask(subtask2.getId());
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список всех подзадач не пустой");
    }

    @Test
    void shouldReturnTaskByCorrectId() {
        manager.addNewTask(task);
        Task taskById = manager.getTask(task.getId());
        assertEquals(task, taskById, "Полученная задача не равна искомой задаче");
    }

    @Test
    void shouldReturnNullTaskByIncorrectId() {
        assertNull(manager.getTask(10), "Не возвращается null");
    }

    @Test
    void shouldReturnEpicByCorrectId() {
        manager.addNewEpic(epic);
        Epic epicById = manager.getEpic(epic.getId());
        assertEquals(epic, epicById, "Полученный эпик не равен искомому эпику");
    }

    @Test
    void shouldReturnNullEpicByIncorrectId() {
        assertNull(manager.getEpic(10), "Не возвращается null");
    }

    @Test
    void shouldReturnSubtaskByCorrectId() {
        manager.addNewSubtask(subtask1);
        Subtask subtaskById = manager.getSubtask(subtask1.getId());
        assertEquals(subtask1, subtaskById, "Полученная подзадача не равна искомой подзадаче");
    }

    @Test
    void shouldReturnNullSubtaskByIncorrectId() {
        assertNull(manager.getSubtask(10), "Не возвращается null");
    }


    @Test
    void shouldReturnListContainsEpicSubtasksByEpicIfEpicContainsTwoSubtasks() {
        manager.addNewEpic(epic);
        List<Subtask> allSubtaskByEpic = new ArrayList<>(manager.getAllSubtaskInEpic(epic.getId()));
        List<Integer> subtaskIdList = epic.getSubtaskId();
        for (Integer subtaskId : subtaskIdList) {
            assertTrue(allSubtaskByEpic.contains(manager.getSubtask(subtaskId)),
                    "Список всех подзадач не содержит задачи созданного эпика с задачами");
        }
    }

    @Test
    void shouldReturnListWithoutDeletedTaskAfterDeleteTaskById() {
        manager.addNewTask(task);

        manager.deleteTask(task.getId());
        assertFalse(manager.getAllTasks().contains(task),
                "После удаления задачи по id, список всех задач содержит удаленную задачу");
    }

    @Test
    void shouldReturnListWithoutDeletedEpicAfterDeleteEpicById() {
        manager.addNewEpic(epic);

        manager.deleteEpic(epic.getId());
        assertFalse(manager.getAllEpics().contains(epic),
                "После удаления эпика по id, список всех эпиков содержит удаленный эпик");
    }

    @Test
    void shouldReturnListWithoutDeletedSubtaskByEpicAfterDeleteEpicById() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.deleteEpic(epic.getId());
        assertFalse(manager.getAllSubtasks().contains(subtask1),
                "Список всех подзадач содержит подзадачи удаленного эпика");
    }

    @Test
    void shouldReturnListWithoutDeletedSubtaskAfterDeleteSubtaskById() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);

        manager.deleteSubtaskById(subtask1.getId());
        assertFalse(manager.getAllSubtasks().contains(subtask1),
                "Список всех подзадач содержит удаленную подзадачу");
    }

    @Test
    void shouldReturnListWithEpicAfterDeleteSubtaskById() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.deleteSubtaskById(subtask1.getId());
        assertTrue(manager.getAllEpics().contains(epic),
                "Список всех эпиков не содержит эпик удаленной подзадачи");
    }

    @Test
    void shouldReturnUpdatedWithChangedStatusAfterUpdateTask() {
        manager.addNewTask(task);

        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getTask(task.getId()).getStatus(),
                "Статус подзадачи не IN_PROGRESS");
    }

    @Test
    void shouldReturnUpdatedEpicWithChangedStatusAfterUpdateEpic() {
        manager.addNewEpic(epic);

        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(epic.getId()).getStatus(),
                "Статус эпика не IN_PROGRESS");
    }

    @Test
    void shouldReturnUpdatedSubtaskWithChangedStatusAfterUpdateSubtask() {
        manager.addNewSubtask(subtask1);

        subtask1.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getSubtask(subtask1.getId()).getStatus(),
                "Статус подзадачи не IN_PROGRESS");
    }

    @Test
    void shouldReturnHistoryListWithSize3AfterViewThreeTasks() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);

        manager.getEpic(epic.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());

        assertFalse(manager.getHistory().isEmpty(), "История пустая");
        assertEquals(3, manager.getHistory().size(), "Размер истории после просмотра трех задач не равен 3");
    }

    @Test
    void shouldThrowWhenAddTwoTaskWithSameStartTimeAndDuration() {
        Task task1 = new Task("Task #2", "Description Task #1", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0), 70);

        manager.addNewTask(task1);

        Task task2 = new Task("Task #3", "Description Task #2", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0),
                10);

        assertThrows(ManagerValidateException.class,
                () -> manager.addNewTask(task2),
                "Creating tasks when they intersect");
    }

    @Test
    void shouldReturnCorrectPrioritizedTasks() {
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(subtask1, prioritizedTasks.get(0),
                "The first priority task is not equal to the first task in the priority list");
        assertEquals(subtask2, prioritizedTasks.get(1),
                "The second priority task is not equal to the second task in the priority list");
    }

    @Test
    void shouldThrowIfCreateTwoTaskWithIntersection(){
        task = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0),
                10);

        manager.addNewTask(task);

        Task newTask = new Task("Task #3", "Description Task #2", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 9, 0, 0),
                80);
        assertThrows(ManagerValidateException.class,
                () -> manager.addNewTask(newTask),
                "Нет ошибки о времени пересечения");
    }

    @Test
    void shouldThrowIfCreateTwoSubtaskWithIntersection(){
        manager.addNewEpic(epic);
        subtask1 = new Subtask("Title Task", "Description Task", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 10, 0, 0),
                60);

        manager.addNewSubtask(subtask1);

        subtask2 = new Subtask("Title Task", "Description Task", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, 1, 1, 10, 30, 0),
                70);

        assertThrows(ManagerValidateException.class,
                () -> manager.addNewSubtask(subtask1),
                "Нет ошибки о времени пересечения");
    }

    @Test
    void shouldThrowIfUpdateTaskWithIntersection(){
        task = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0),
                60);

        Task task2 = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 11, 0, 0),
                10);
        manager.addNewTask(task);
        manager.addNewTask(task2);

        Task newTask = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 30, 0),
                60);
        newTask.setId(task.getId());

        assertThrows(ManagerValidateException.class,
                () -> manager.updateTask(newTask),
                "Нет ошибки о времени пересечения");
    }

    @Test
    void shouldThrowIfUpdateSubtaskWithIntersection(){
        manager.addNewEpic(epic);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());

        Subtask newSubtask = new Subtask("Title Subtask", "Description Subtask",
                Status.NEW, subtask2.getEpicId(),
                LocalDateTime.of(2023, 1, 1, 11, 0, 0), 60);
        newSubtask.setId(subtask2.getId());

        assertThrows(ManagerValidateException.class,
                () -> manager.updateSubtask(newSubtask),
                "Нет ошибки о времени пересечения");
    }
}