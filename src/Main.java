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

        Epic epic1 = new Epic(1, "Epic #1", "Epic1 description", Status.NEW);
        Epic epic2 = new Epic(2, "Epic #2", "Epic2 description", Status.IN_PROGRESS);

        Subtask subtask1 = new Subtask(1, "Subtask #1-1", "Subtask1 description", Status.NEW);
        Subtask subtask2 = new Subtask(2, "Subtask #1-2", "Subtask2 description", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask(2, "Subtask #2-2", "Subtask2 description", Status.DONE);
        Subtask subtask4 = new Subtask(2, "Subtask #2-1", "Subtask2 description", Status.DONE);


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



    }

}
