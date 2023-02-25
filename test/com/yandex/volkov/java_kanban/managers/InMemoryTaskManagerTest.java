package com.yandex.volkov.java_kanban.managers;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    @BeforeEach
    public void createTaskManager(){
        manager = new InMemoryTaskManager(Manager.getDefaultHistory());
    }

}