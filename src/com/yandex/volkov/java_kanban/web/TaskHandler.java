package com.yandex.volkov.java_kanban.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.volkov.java_kanban.adapters.DurationAdapter;
import com.yandex.volkov.java_kanban.adapters.LocalDateTimeAdapter;
import com.yandex.volkov.java_kanban.exceptions.HttpException;
import com.yandex.volkov.java_kanban.managers.TaskManager;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TaskHandler implements HttpHandler {
    private final TaskManager httpManager;
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();
    }

    public TaskHandler(TaskManager httpManager) {
        this.httpManager = httpManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String[] partPath = uri.getPath().split("/");



        try {
            String typeController;
            if (partPath.length == 2) {
                controller(exchange, "tasks");
                return;
            }
            controller(exchange, partPath[2]);
        } catch (IOException e) {
            exchange.sendResponseHeaders(400, 0);
            throw new RuntimeException("Incorrect request");
        }
    }

    private void controller(HttpExchange exchange, String typeController ) throws IOException {
        switch (typeController) {
            case "task":
                taskHandler(exchange);
                break;
            case "tasks":
                tasksHandler(exchange);
                break;
            case "subtask":
                subtaskHandler(exchange);
                break;
            case "epic":
                epicHandler(exchange);
                break;
            case "history":
                HistoryHandler(exchange);
                break;
            default:
                exchange.sendResponseHeaders(405, 0);
                break;
        }
    }

    private void writeResponse(HttpExchange exchange, String response, int statusCode) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(statusCode, 0);
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new HttpException("Error of getting response.");
        }
    }

    private void tasksHandler(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            String response = GSON.toJson(httpManager.getPrioritizedTasks());
            writeResponse(exchange, response, 200);
        }
        writeResponse(exchange, "", 405);
    }

    private void taskHandler(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        int id = getIdFromQuery(exchange.getRequestURI());

        if (method.equals("GET")) {
            if (id != 0) {
                String response;
                Task task = httpManager.getTask(id);
                if (task == null) {
                    response = "";
                } else {
                    response = GSON.toJson(task);
                }
                writeResponse(exchange, response, 200);
                return;
            }
            String response = GSON.toJson(httpManager.getAllTasks());
            writeResponse(exchange, response, 200);
            return;
        }

        if (method.equals("DELETE")) {
            if (id != 0) {
                httpManager.deleteTask(id);
                writeResponse(exchange, "Task with id=".concat(Integer.toString(id).concat( " has been  deleted")), 200);
                return;
            }
            httpManager.clearTask();
            writeResponse(exchange, "Tasks epics has been deleted", 200);
            return;

        }

        if (method.equals("POST")) {
            String strId = getIdFromBodyRequest(exchange);

            try (InputStream inputStream = exchange.getRequestBody()) {
                String strBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task newTask = GSON.fromJson(strBody, Task.class);
                if (strId.isEmpty()) {
                    httpManager.addNewTask(newTask);
                    writeResponse(exchange, "New task has been created", 200);
                } else {
                    httpManager.updateTask(newTask);
                    writeResponse(exchange, "Task with id=".concat(Integer.toString(newTask.getId()).concat(" has been  updated")), 200);
                }
            }
        }
    }

    private void epicHandler(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        int id = getIdFromQuery(exchange.getRequestURI());

        if (method.equals("GET")) {
            if (id != 0) {
                String response;
                Epic epic = httpManager.getEpic(id);
                if(epic == null){
                    response = "";
                }else {
                    response = GSON.toJson(epic);
                }
                writeResponse(exchange, response, 200);
                return;
            }
            String response = GSON.toJson(httpManager.getAllEpics());
            writeResponse(exchange, response, 200);
        }

        if (method.equals("DELETE")) {
            if (id != 0) {
                httpManager.deleteEpic(id);
                writeResponse(exchange, "Epic with id=".concat(Integer.toString(id).concat( " has been deleted")), 200);
                return;
            }
            httpManager.clearEpic();
            writeResponse(exchange, "All epics has been deleted", 200);
            return;
        }

        if (method.equals("POST")) {
            String strId = getIdFromBodyRequest(exchange);

            try (InputStream inputStream = exchange.getRequestBody()) {
                String strBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic newEpic = GSON.fromJson(strBody, Epic.class);
                if (strId.isEmpty()) {
                    httpManager.addNewEpic(newEpic);
                    writeResponse(exchange, "New epic has been created", 200);
                } else {
                    httpManager.updateEpic(newEpic);
                    writeResponse(exchange, "Epic with id=".concat(Integer.toString(newEpic.getId()).concat(" has been  updated")), 200);
                }
            }
        }
    }

    private void subtaskHandler(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        int id = getIdFromQuery(exchange.getRequestURI());

        if (method.equals("GET")) {
            URI uri = exchange.getRequestURI();
            String[] partPath = uri.getPath().split("/");
            if (partPath.length == 3 && id != 0) {
                String response;
                Subtask subtask = httpManager.getSubtask(id);
                if(subtask == null){
                    response = "";
                }else {
                    response = GSON.toJson(subtask);
                }
                writeResponse(exchange, response, 200);
                return;
            }
            if (partPath.length == 4 && id != 0) {
                String response = GSON.toJson(httpManager.getAllSubtaskInEpic(id));
                writeResponse(exchange, response, 200);
                return;
            }
            String response = GSON.toJson(httpManager.getAllSubtasks());
            writeResponse(exchange, response, 200);
        }

        if (method.equals("DELETE")) {
            if (id != 0) {
                httpManager.deleteSubtask(id);
                writeResponse(exchange, "Subtask with id=".concat(Integer.toString(id).concat( " has been deleted")), 200);
                return;
            }
            httpManager.clearSubtask();
            writeResponse(exchange, "All subtask has been deleted", 200);
            return;
        }

        if (method.equals("POST")) {
            String strId = getIdFromBodyRequest(exchange);

            try (InputStream inputStream = exchange.getRequestBody()) {
                String strBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Subtask newSubtask = GSON.fromJson(strBody, Subtask.class);
                if (strId.isEmpty()) {
                    httpManager.addNewSubtask(newSubtask);
                    writeResponse(exchange, "New subtask has been created", 200);
                } else {
                    httpManager.updateSubtask(newSubtask);
                    writeResponse(exchange, "Subtask with id=".concat(Integer.toString(newSubtask.getId()).concat(" has been  updated")), 200);
                }
            }
        }
    }

    private void HistoryHandler(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            String response = GSON.toJson(httpManager.getHistory());
            writeResponse(exchange, response, 200);
        }
    }



    private int getIdFromQuery(URI uri) {
        String query = uri.getQuery();
        if (query != null) {
            String strId = query.substring(query.indexOf("=") + 1);
            return Integer.parseInt(strId);
        }
        return 0;
    }

    private String getIdFromBodyRequest(HttpExchange exchange) {
        Map<String, String> map = new HashMap<>();
        if (exchange.getRequestURI().getQuery() != null) {
            String[] partOfQuery = exchange.getRequestURI().getQuery().split("&");
            for (String part : partOfQuery) {
                String[] split = part.split("=");
                map.put(split[0], split[1]);
            }
        }
        return map.getOrDefault("id", "");
    }

}
