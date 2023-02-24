package managers;

import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.InMemoryTaskManager;
import com.yandex.volkov.java_kanban.managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    @BeforeEach
    public void createTaskManager(){
        manager = new InMemoryTaskManager(Manager.getDefaultHistory());
    }

}