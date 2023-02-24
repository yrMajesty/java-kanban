package com.yandex.volkov.java_kanban.managers;


import com.yandex.volkov.java_kanban.web.HttpTaskManager;
import com.yandex.volkov.java_kanban.web.KVServer;

public class Manager {

    public static HttpTaskManager getDefault(){
        return new HttpTaskManager(KVServer.URL);
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

}





