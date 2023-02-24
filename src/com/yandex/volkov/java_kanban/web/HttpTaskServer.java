package com.yandex.volkov.java_kanban.web;

import com.sun.net.httpserver.HttpServer;
import com.yandex.volkov.java_kanban.managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    public static final String URL = "http://localhost:".concat(Integer.toString(PORT)).concat("/");
    private final HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager));
    }

    public void start() {
        server.start();
        System.out.println("HttpTaskServer на порту ".concat(Integer.toString(PORT).concat(" запущен")));
    }

    public void stop(){
        server.stop(1);
        System.out.println("HttpTaskServer на порту ".concat(Integer.toString(PORT).concat(" остановлен")));
    }
}
