package com.yandex.volkov.java_kanban.managers;

import com.yandex.volkov.java_kanban.exceptions.ManagerValidateException;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
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
            if(isIntersectionTasksByTime(task)){
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
            if(isIntersectionTasksByTime(subtask)){
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

            if(isIntersectionTasksByTime(task)){
                throw new ManagerValidateException(
                        "Обнаружено пересечение задач");
            }
            prioritizedTasks.add(task);
            taskMap.put(task.getId(), task);
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

        if(isIntersectionTasksByTime(subtask)){
            throw new ManagerValidateException("Обнаружено пересечение задач");
        }
        prioritizedTasks.add(subtask);

        subtaskMap.put(subtask.getId(), subtask);

        Integer epicId = subtask.getEpicId();
        if(epicId != null){
            Epic epic = epicMap.get(epicId);
            if(epic != null){
                epic.getSubtaskId().add(subtask.getId());
                updateEpicStatus(epic);
                updateDurationEpic(epic);
            }
        }else {
            System.out.println("Данные с EPIC отсутствуют");
            return null;
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
    public void clearSubtask() {
        for (Subtask subtask : subtaskMap.values()) {
            Integer epicId = subtask.getEpicId();
            if(epicId != null){
                epicMap.remove(epicId);
            }
        }
        subtaskMap.clear();
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
    public void deleteSubtask(int id) {
        Subtask removableSubtask = subtaskMap.get(id);
        if(removableSubtask != null) {
            int epicID = removableSubtask.getEpicId();
            if(epicID != 0){
                epicMap.get(epicID).getSubtaskId().removeIf(idSubtask -> idSubtask.equals(id));
                updateEpicStatus(epicMap.get(epicID));
            }
            prioritizedTasks.remove(removableSubtask);
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
            if(isIntersectionTasksByTime(task)){
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
            if(isIntersectionTasksByTime(subtask)){
                throw new ManagerValidateException(
                        "Обнаружено пересечение задач");
            }
            prioritizedTasks.add(subtask);
            subtaskMap.put(subtask.getId(), subtask);
            Epic epic = epicMap.get(subtask.getEpicId());
            updateEpicStatus(epic);
            updateDurationEpic(epic);
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

    private void updateDurationEpic(Epic epic) {
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        Duration duration = Duration.ZERO;
        List<Integer> epicSubtaskId = epic.getSubtaskId();
        for (Integer idSubtask : epicSubtaskId) {
            Subtask subtask = subtaskMap.get(idSubtask);
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
            duration = duration.plus(subtask.getDuration());
        }
        epic.setStartTime(startTime);
        epic.setDuration(duration);
        epic.setEndTime(endTime);
    }

    public boolean isIntersectionTasksByTime(Task newTask) {
        LocalDateTime newTaskStart = newTask.getStartTime();
        LocalDateTime newTaskFinish = newTask.getEndTime();
        for (Task task : prioritizedTasks) {
            if(Objects.equals(task.getId(), newTask.getId())){
                continue;
            }
            LocalDateTime taskStart = task.getStartTime();
            LocalDateTime taskFinish = task.getEndTime();
            if(newTaskStart.equals(taskStart) || newTaskFinish.equals(taskFinish)){
                return true;
            }
            if(newTaskStart.isAfter(taskStart) && newTaskFinish.isBefore(taskFinish)){
                return true;
            }
            if(newTaskStart.isBefore(taskStart) && newTaskFinish.isAfter(taskFinish)){
                return true;
            }
            if(newTaskFinish.isAfter(taskStart) && newTaskStart.isBefore(taskFinish)){
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





