import service.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

      Task task1 = new Task("Task #1", "Task1 description", Status.NEW);
        Task task2 = new Task("Task #2", "Task2 description", Status.IN_PROGRESS);

        Epic epic1 = new Epic("Epic #1", "Epic1 description", Status.NEW);
        Epic epic2 = new Epic("Epic #2", "Epic2 description", Status.IN_PROGRESS);

        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", Status.NEW);
        Subtask subtask2 = new Subtask("Subtask #1-2", "Subtask2 description", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask("Subtask #2-2", "Subtask2 description", Status.DONE);
        Subtask subtask4 = new Subtask("Subtask #2-1", "Subtask2 description", Status.DONE);


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


        /*final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);

        final Integer subtaskId1 = manager.addNewSubtask(subtask1, epicId1);
        final Integer subtaskId2 = manager.addNewSubtask(subtask2, epicId1);
        final Integer subtaskId3 = manager.addNewSubtask(subtask3, epicId2);
        final Integer subtaskId4 = manager.addNewSubtask(subtask4, epicId2);*/

    }

}
