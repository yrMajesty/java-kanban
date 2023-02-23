package com.yandex.volkov.java_kanban.managers.task;

import com.yandex.volkov.java_kanban.managers.history.HistoryManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int nextId = 0;
    protected final Set<Integer> idSet = new HashSet<>();
    protected Map<Integer, Task> taskMap = new HashMap<>();
    protected Map<Integer, Epic> epicMap = new HashMap<>();
    protected Map<Integer, Subtask> subtaskMap = new HashMap<>();
    protected HistoryManager historyManager;
    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    public Map<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }

    public Comparator<Task> getTaskComparator() {
        return taskComparator;
    }

    @Override
    public List<Epic> getAllEpic() {
        if (epicMap.size() == 0) {
            System.out.println("Данные с EPIC отсутсвуют");
            return null;
        }
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Task> getAllTask() {
        if (taskMap.size() == 0) {
            System.out.println("Данные с TASK отсутствуют");
            return null;
        }
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
        if (subtaskMap.size() == 0) {
            System.out.println("Данные с SUBTASK отсутствуют");
            return Collections.emptyList();
        }
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void addToHistory(int id) {
        if (epicMap.containsKey(id)) {
            historyManager.add(epicMap.get(id));
        } else if (subtaskMap.containsKey(id)) {
            historyManager.add(subtaskMap.get(id));
        } else if (taskMap.containsKey(id)) {
            historyManager.add(taskMap.get(id));
        }
    }

    @Override
    public void remove(int id) {
        historyManager.remove(id);
    }

    @Override
    public Task getTask(int id) {
        Task task = taskMap.get(id);
        if (task != null) {
            if(!checkIntersectionTasksByTime(task)){
                throw new ManagerValidateException(
                        "Обнаружено пересечение задач");
            }
            prioritizedTasks.add(task);
            historyManager.add(taskMap.get(id));
        }
        return taskMap.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            historyManager.add(epicMap.get(id));
        }
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtaskMap.get(id);
        if (subtask != null) {
            if(!checkIntersectionTasksByTime(subtask)){
                throw new ManagerValidateException(
                        "Обнаружено пересечение задач");
            }
            prioritizedTasks.add(subtask);
            historyManager.add(subtaskMap.get(id));
        }
        return subtaskMap.get(id);
    }

    @Override
    public List<Epic> getAllEpics() {
        if (epicMap.size() == 0) {
            System.out.println("Данные с EPIC отсутствуют");
            return Collections.emptyList();
        }
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        if (subtaskMap.size() == 0) {
            System.out.println("Данные с SUBTASK отсутствуют");
            return Collections.emptyList();
        }
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public List<Task> getAllTasks() {
        if (taskMap.size() == 0) {
            System.out.println("Данные с TASK отсутствуют");
            return Collections.emptyList();
        }
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public Task addNewTask(Task task) {
        if (task == null) {
            return null;
        } else {
            task.setId(getNextId());
            taskMap.put(task.getId(), task);
            if(checkIntersectionTasksByTime(task)){
                throw new ManagerValidateException(
                        "Обнаружено пересечение задач");
            }
            prioritizedTasks.add(task);
            return task;
        }
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        if (epic == null) return null;
        epic.setId(getNextId());
        epicMap.put(epic.getId(), epic);
        for (Subtask subtask : subtaskMap.values()) {
            if (epic.getId().equals(subtask.getEpicId())) {
                epic.getSubtaskId().add(subtask.getId());
            }
        }
        return epic;
    }

    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        if (subtask == null) return null;
        subtask.setId(getNextId());
        subtaskMap.put(subtask.getId(), subtask);
        if(checkIntersectionTasksByTime(subtask)){
            throw new ManagerValidateException("Обнаружено пересечение задач");
        }
        prioritizedTasks.add(subtask);
        for (Epic epic : epicMap.values()) {
            if (epic != null) {
                if (epic.getId().equals(subtask.getEpicId())) {
                    epic.getSubtaskId().add(subtask.getId());
                }
                if (epicMap.containsKey(subtask.getEpicId())) {
                    updateEpicStatus(epicMap.get(subtask.getEpicId()));
                }
                changeDurationEpic(epic);
            } else {
                System.out.println("Данные с EPIC отсутствуют");
                return null;
            }
        }
        return subtask;
    }

    private int getNextId() {
        do {
            nextId++;
        } while (idSet.contains(nextId));
        return nextId;
    }

    @Override
    public void clearTask() {
        taskMap.clear();
    }

    @Override
    public void clearEpic() {
        subtaskMap.clear();
        epicMap.clear();
    }

    @Override
    public void clearSubtask(int id) {
        subtaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epicMap.get(id));
        }
    }

    @Override
    public void deleteTask(int id) {
        Task removableTask = taskMap.get(id);
        if(removableTask != null) {
            prioritizedTasks.remove(removableTask);
            taskMap.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic removableEpic = epicMap.get(id);
        if (removableEpic == null || epicMap.get(id).getSubtaskId() == null) return;

        List<Integer> subEpic = epicMap.get(id).getSubtaskId();
        for (int subtaskId : subEpic) {
            historyManager.remove(subtaskId);
            subtaskMap.remove(subtaskId);
            prioritizedTasks.remove(removableEpic);
        }
        historyManager.remove(id);
        epicMap.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        Subtask removableSubtask = subtaskMap.get(id);
        if(removableSubtask != null) {
            int epicID = subtaskMap.get(id).getEpicId();
            epicMap.get(epicID).getSubtaskId().remove(id);
            prioritizedTasks.remove(removableSubtask);
            updateEpicStatus(epicMap.get(epicID));
            historyManager.remove(subtaskMap.get(id).getId());
            subtaskMap.remove(id);
        }
    }

    @Override
    public List<Subtask> getAllSubtaskInEpic(int id) {

        List<Subtask> subtasksByEpic = new ArrayList<>();
        List<Integer> subList = epicMap.get(id).getSubtaskId();

        for (Integer subId : subList) {

            subtasksByEpic.add(subtaskMap.get(subId));
        }
        return subtasksByEpic;

    }

    @Override
    public void updateTask(Task task) {
        if (task != null && taskMap.containsKey(task.getId())) {
            if(checkIntersectionTasksByTime(task)){
                throw new ManagerValidateException(
                        "Обнаружено пересечение задач");
            }
            prioritizedTasks.add(task);
            taskMap.put(task.getId(), task);
        } else {
            System.out.println("Task не найден");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epicMap.containsKey(epic.getId())) {
            epicMap.put(epic.getId(), epic);
            updateEpicStatus(epic);
        } else {
            System.out.println("Epic не найден");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtaskMap.containsKey(subtask.getId())) {
            if(checkIntersectionTasksByTime(subtask)){
                throw new ManagerValidateException(
                        "Обнаружено пересечение задач");
            }
            prioritizedTasks.add(subtask);
            subtaskMap.put(subtask.getId(), subtask);
            Epic epic = epicMap.get(subtask.getEpicId());
            updateEpicStatus(epic);
            changeDurationEpic(epic);
        } else {
            System.out.println("Subtask not found");
        }
    }

    private void updateEpicStatus(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        Status value;
        if (epicMap.get(epic.getId()).getSubtaskId().isEmpty()) {
            epicMap.get(epic.getId()).setStatus(Status.NEW);
        } else {
            for (int sub : epicMap.get(epic.getId()).getSubtaskId()) {
                value = subtaskMap.get(sub).getStatus();
                if (value == Status.NEW) {
                    statusNew++;
                }
                if (value == Status.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == epicMap.get(epic.getId()).getSubtaskId().size()) {
                epicMap.get(epic.getId()).setStatus(Status.NEW);
            } else if (statusDone == epicMap.get(epic.getId()).getSubtaskId().size()) {
                epicMap.get(epic.getId()).setStatus(Status.DONE);
            } else {
                epicMap.get(epic.getId()).setStatus(Status.IN_PROGRESS);
            }
        }
    }

    private void changeDurationEpic(Epic epic) {
        LocalDateTime startTime = LocalDateTime.MAX;
        long duration = 0;
        List<Integer> epicSubtaskId = epic.getSubtaskId();
        for (Integer idSubtask : epicSubtaskId) {
            Subtask subtask = subtaskMap.get(idSubtask);
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            duration = ChronoUnit.MINUTES.between(startTime, subtask.getEndTime());
        }
        LocalDateTime endTime = startTime.plusMinutes(duration);
        epic.setStartTime(startTime);
        epic.setDuration(duration);
        epic.setEndTime(endTime);
    }

    public boolean checkIntersectionTasksByTime(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
        LocalDateTime newTaskStart = task.getStartTime();
        LocalDateTime newTaskFinish = task.getEndTime();
        for (Task taskSave : tasks) {
            LocalDateTime currentTaskStart = taskSave.getStartTime();
            LocalDateTime currentTaskEnd = taskSave.getEndTime();
            if (newTaskStart.isBefore(currentTaskStart) && newTaskFinish.isAfter(currentTaskStart)) {
                return true;
            }
            if (newTaskStart.isBefore(currentTaskEnd) && newTaskFinish.isAfter(currentTaskEnd)) {
                return true;
            }
            if (newTaskStart.isEqual(currentTaskStart) && newTaskFinish.isBefore(currentTaskEnd)) {
                return true;
            }
            if (newTaskStart.isEqual(currentTaskStart) && newTaskFinish.isEqual(currentTaskEnd)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

}





