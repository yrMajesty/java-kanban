package task;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(int id, String title, String descriptions, Status status) {
        super(id, title, descriptions, status);
    }

    public Subtask (String title, String descriptions, Status status) {
        super(title, descriptions, status);
    }

    public int getEpicId() { return epicId;  }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", descriptions='" + getDescriptions() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }

}
