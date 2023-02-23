
import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.task.InMemoryTaskManager;
import com.yandex.volkov.java_kanban.managers.task.TaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    @BeforeEach
    public void createTaskManager(){
        manager = new InMemoryTaskManager(Manager.getDefaultHistory());
    }

}