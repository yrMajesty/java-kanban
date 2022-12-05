package task;

import java.util.ArrayList;

public class Epic extends Task {

protected ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(int id, String title, String descriptions, Status status) {
        super( id, title, descriptions, status);
    }

    public Epic(String title, String descriptions, Status status) {
        super(title, descriptions, status);
    }

    public boolean isEpic() {
        return true;
    }

    public void addSubtaskId(int id) {
        subtaskId.add(id);
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int id) {
        subtaskId.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", descriptions='" + getDescriptions() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
