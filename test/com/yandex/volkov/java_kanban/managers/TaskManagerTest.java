package com.yandex.volkov.java_kanban.managers;

import com.yandex.volkov.java_kanban.exceptions.ManagerValidateException;
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
    public void initTasks() {
        task = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 2, 1, 10, 0, 0), 10);

        epic = new Epic("Title Epic", "Descr Epic", Status.NEW,
                LocalDateTime.of(2023, 12, 1, 8, 0, 0), 60);

        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 13, 0, 0), 60);
    }

    @Test
    void checkListSubtask_emptyListSubtask_listSubtaskIsEmpty() {
        manager.addNewEpic(epic);
        List<Subtask> listSubtasks = manager.getAllSubtaskInEpic(epic.getId());
        assertTrue(listSubtasks.isEmpty(), "List of all subtask is not empty");
    }

    @Test
    void checkEpicStatus_statusNew_subtaskWithStatusNew() {
        manager.addNewEpic(epic);
        manager.addNewSubtask(subtask1);
        Status statusEpic = manager.getEpic(epic.getId()).getStatus();
        assertEquals(subtask1.getStatus(), statusEpic, "Status of new epic is not NEW");
    }

    @Test
    void checkEpicStatus_statusInProgress_createOneSubtaskWithStatusNewAndOneSubtaskWithStatusDone() {
        manager.addNewEpic(epic);
        subtask2.setStatus(Status.DONE);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(subtask1.getEpicId()).getStatus(),
                "Status of epic with one task(status DONE) and one task (status NEW) is not IN_PROGRESS");
    }

    @Test
    void checkEpicStatus_statusInProgress_createOneSubtaskWithStatusNewAndOneSubtaskWithStatusInProgress() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(subtask1.getEpicId()).getStatus(),
                "Status of epic with two task(status IN_PROGRESS) is not IN_PROGRESS");
    }

    @Test
    void checkEpicStatus_statusDone_createSubtaskWithStatusDone() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        assertEquals(Status.DONE, manager.getEpic(subtask1.getEpicId()).getStatus(),
                "Status of epic with two task(status DONE) is not DONE");
    }

    @Test
    void checkDuration_durationEqualsSumDurationsOfSubtasks_createEpicWithTwoSubtasks() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        assertEquals(epic.getDuration().toMinutes(),subtask1.getDuration().plus(subtask2.getDuration()).toMinutes(),
                "Duration in minutes of epic is not equals sum of duration of subtasks and time between subtasks in epic");
    }

    @Test
    void checkStartTimeEpic_startTimeEpicEqualsStartTimeCreatedSubtask_createOneSubtask(){
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        assertEquals(epic.getStartTime(),subtask1.getStartTime(),
                "Start time epic is not equals start time his subtask");
    }

    @Test
    void checkEndTimeEpic_endTimeEpicEqualsEndTimeCreatedSubtask_createOneSubtask(){
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        assertEquals(epic.getEndTime(),subtask1.getEndTime(),
                "End time epic is not equals end time his subtask");
    }

    @Test
    void checkEndTimeEpic_endTimeEpicEqualsEndTimeHisLastSubtask_createTwoSubtask(){
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        assertEquals(epic.getEndTime(),subtask2.getEndTime(),
                "End time epic is not equals end time his subtask");
    }

    @Test
    void createOneTask_createdTaskWithStatusNew_createTaskWithStatusNew() {
        manager.addNewTask(task);
        assertEquals(Status.NEW, task.getStatus(), "Статус новой задачи не NEW");
    }

    @Test
    void createOneEpic_createdEpicWithStatusNew_createEpicWithStatusNew() {
        manager.addNewEpic(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Статус только что созданного эпика не NEW");
    }

    @Test
    void createOneSubtask_createdSubtaskWithStatusNew_createSubtaskWithStatusNew() {
        manager.addNewEpic(epic);
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        assertEquals(Status.NEW, subtask1.getStatus(), "Статус только что созданной подзадачи не NEW");
    }

    @Test
    void checkListTasksContainsCreatedTask_listTaskIsNotEmptyAndContainsCreatedTask_createTask() {
        manager.addNewTask(task);
        List<Task> allTasks = new ArrayList<>(manager.getAllTasks());
        assertFalse(allTasks.isEmpty(),
                "Список всех задач пустой");
        assertTrue(allTasks.contains(task),
                "После создания задачи, список всех задач не содержит созданную задачу");
    }

    @Test
    void checkListEpicsContainsCreatedEpic_listEpicIsNotEmptyAndContainsCreatedEpic_createEpic() {
        manager.addNewEpic(epic);
        List<Epic> allEpics = new ArrayList<>(manager.getAllEpics());
        assertFalse(allEpics.isEmpty(),
                "Список всех эпиков пустой");
        assertTrue(allEpics.contains(epic),
                "После создания эпика, список всех эпиков не содержит созданный эпик");
    }

    @Test
    void checkListSubtasksContainsCreatedSubtask_listSubtaskIsNotEmptyAndContainsCreatedSubtask_createSubtask() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        List<Subtask> allSubtasks = new ArrayList<>(manager.getAllSubtasks());

        assertFalse(allSubtasks.isEmpty(), "Список всех подзадач пустой");
        assertTrue(allSubtasks.contains(subtask1),
                "После создания подзадачи, список всех подзадач не содержит созданную подзадачу");
    }

    @Test
    void checkListTasksIsEmpty_emptyListTasks_didNotCreateTasks() {
        Collection<Task> allTasks = manager.getAllTasks();
        assertTrue(allTasks.isEmpty(), "Список всех задач не пустой");
    }

    @Test
    void checkListEpicIsEmpty_emptyListEpics_didNotCreateEpics(){
        Collection<Epic> allEpics = manager.getAllEpics();
        assertTrue(allEpics.isEmpty(), "Список всех эпиков не пустой");
    }

    @Test
    void checkListSubtasksIsEmpty_emptyListSubtasks_didNotCreateSubtasks() {
        Collection<Subtask> allSubtasks = manager.getAllSubtasks();
        assertTrue(allSubtasks.isEmpty(), "Список всех подзадач не пустой");
    }

    @Test
    void checkListTasksIsEmpty_emptyListTasks_createdTaskAndRemovedAllTasks() {
        manager.addNewTask(task);
        assertFalse(manager.getAllTasks().isEmpty(), "Список всех задач пустой");
        manager.clearTask();
        assertTrue(manager.getAllTasks().isEmpty(), "Список всех эпиков не пустой");
    }

    @Test
    void checkListEpicsIsEmpty_emptyListEpics_createEpicAndRemovedAllEpics() {
        manager.addNewEpic(epic);
        assertFalse(manager.getAllEpics().isEmpty(), "Список всех эпиков пустой");

        manager.clearEpic();
        assertTrue(manager.getAllEpics().isEmpty(), "Список всех эпиков не пустой");
    }


    @Test
    void checkListSubtasksIsEmpty_emptyListSubtasks_createEpicAndSubtasksAndRemovedAllEpics() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask2);
        assertFalse(manager.getAllSubtasks().isEmpty(), "Список всех подзадач пустой");
    }

    @Test
    void checkListSubtasksIsEmpty_emptyListSubtasks_createEpicAndSubtasksAndRemovedCreatedSubtasks() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask2);

        assertFalse(manager.getAllSubtasks().isEmpty(), "Список всех подзадач пустой");

        manager.clearSubtask();
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список всех подзадач не пустой");
    }

    @Test
    void getTaskById_rightTask_getTaskByCorrectId() {
        manager.addNewTask(task);
        Task taskById = manager.getTask(task.getId());
        assertEquals(task, taskById, "Полученная задача не равна искомой задаче");
    }

    @Test
    void getTaskById_null_getTaskByIncorrectId() {
        assertNull(manager.getTask(10), "Не возвращается null");
    }

    @Test
    void getEpicById_rightEpic_getEpicByCorrectId() {
        manager.addNewEpic(epic);
        Epic epicById = manager.getEpic(epic.getId());
        assertEquals(epic, epicById, "Полученный эпик не равен искомому эпику");
    }

    @Test
    void getEpicById_returnNull_getEpicByIncorrectId(){
        assertNull(manager.getEpic(10), "Не возвращается null");
    }

    @Test
    void getSubtaskById_rightSubtask_getSubtaskByCorrectId() {
        manager.addNewSubtask(subtask1);
        Subtask subtaskById = manager.getSubtask(subtask1.getId());
        assertEquals(subtask1, subtaskById, "Полученная подзадача не равна искомой подзадаче");
    }

    @Test
    void getSubtaskById_null_getSubtaskByIncorrectId(){
        assertNull(manager.getSubtask(10), "Не возвращается null");
    }

    @Test
    void getSubtaskIdListByEpic_listContainsIdCreatedSubtasks_createEpicAndSubtasks() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        subtask2.setEpicId(epic.getId());
        manager.addNewSubtask(subtask2);

        List<Subtask> allSubtaskByEpic = new ArrayList<>(manager.getAllSubtaskInEpic(epic.getId()));
        List<Integer> subtaskIdList = epic.getSubtaskId();
        for (Integer subtaskId : subtaskIdList) {
            assertTrue(allSubtaskByEpic.contains(manager.getSubtask(subtaskId)),
                    "Список всех подзадач не содержит задачи созданного эпика с задачами");
        }
    }

    @Test
    void deleteTaskById_listDoesNotContainsDeletedTask_createTaskAndDeleteTaskById() {
        manager.addNewTask(task);
        manager.deleteTask(task.getId());
        assertFalse(manager.getAllTasks().contains(task),
                "После удаления задачи по id, список всех задач содержит удаленную задачу");
    }

    @Test
    void deleteEpicById_listDoesNotContainsDeletedEpic_createEpicAndDeleteEpicById() {
        manager.addNewEpic(epic);
        manager.deleteEpic(epic.getId());
        assertFalse(manager.getAllEpics().contains(epic),
                "После удаления эпика по id, список всех эпиков содержит удаленный эпик");
    }

    @Test
    void deleteSubtaskById_listDoesNotContainsSubtaskDeletedEpic_createSubtaskAndEpicAndDeleteEpicById() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.deleteEpic(epic.getId());
        assertFalse(manager.getAllSubtasks().contains(subtask1),
                "Список всех подзадач содержит подзадачи удаленного эпика");
    }

    @Test
    void deleteSubtaskById_listDoesNotContainsDeletedSubtask_createSubtaskAndEpicAndDeleteSubtaskById() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.deleteSubtask(subtask1.getId());
        assertFalse(manager.getAllSubtasks().contains(subtask1),
                "Список всех подзадач содержит удаленную подзадачу");
    }

    @Test
    void deleteSubtaskById_listDoesNotContainsEpicDeletedSubtask_createSubtaskAndEpicAndDeleteSubtaskById() {
        manager.addNewEpic(epic);
        subtask1.setEpicId(epic.getId());
        manager.addNewSubtask(subtask1);
        manager.deleteSubtask(subtask1.getId());
        assertTrue(manager.getAllEpics().contains(epic),
                "Список всех эпиков не содержит эпик удаленной подзадачи");
    }

    @Test
    void updateTask_correctStatus_setNewStatusAndUpdateTask() {
        manager.addNewTask(task);
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getTask(task.getId()).getStatus(),
                "Статус подзадачи не IN_PROGRESS");
    }

    @Test
    void updateEpic_correctStatus_setNewStatusAndUpdateEpic(){
        manager.addNewEpic(epic);
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(epic.getId()).getStatus(),
                "Статус эпика не IN_PROGRESS");
    }

    @Test
    void updateSubtask_correctStatus_setNewStatusAndUpdateSubtask() {
        manager.addNewSubtask(subtask1);
        subtask1.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getSubtask(subtask1.getId()).getStatus(),
                "Статус подзадачи не IN_PROGRESS");
    }

    @Test
    void getHistory_size3ListHistory_viewedThreeTasksById() {
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
    void addNewTaskWithIntersection_throw_addTwoTaskWithSameStartTimeAndDuration() {
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
    void getPrioritizedTasks_correctFirstAndSecondTaskByStartTime_addedTwoNewTasks() {
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(subtask1, prioritizedTasks.get(0),
                "The first priority task is not equal to the first task in the priority list");
        assertEquals(subtask2, prioritizedTasks.get(1),
                "The second priority task is not equal to the second task in the priority list");
    }

    @Test
    void addNewSubtasksWithIntersection_throw_addTwoSubtasksWithIntersectionByTime(){
        manager.addNewEpic(epic);
        manager.addNewSubtask(subtask1);
        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 9, 0, 0), 90);

        assertThrows(ManagerValidateException.class,
                () -> manager.addNewSubtask(subtask1),
                "Нет ошибки о времени пересечения");
    }

    @Test
    void updateTask_throw_updatableTaskWithIntersection(){
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
    void updateSubtask_throw_updatableSubtaskWithIntersection(){
        manager.addNewEpic(epic);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());

        Subtask newSubtask = new Subtask("Title Subtask", "Description Subtask",
                Status.NEW, subtask2.getEpicId(),
                LocalDateTime.of(2023, 1, 1, 9, 0, 0), 90);
        newSubtask.setId(subtask2.getId());

        assertThrows(ManagerValidateException.class,
                () -> manager.updateSubtask(newSubtask),
                "Нет ошибки о времени пересечения");
    }
}