package com.yandex.volkov.java_kanban;

import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.web.HttpTaskServer;
import com.yandex.volkov.java_kanban.web.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer(Manager.getDefault()).start();
    }
}
