package com.yandex.volkov.java_kanban.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.volkov.java_kanban.adapters.DurationAdapter;
import com.yandex.volkov.java_kanban.adapters.LocalDateTimeAdapter;
import com.yandex.volkov.java_kanban.exceptions.HttpException;
import com.yandex.volkov.java_kanban.managers.Manager;
import com.yandex.volkov.java_kanban.managers.TaskManager;
import com.yandex.volkov.java_kanban.managers.TaskManagerTest;
import com.yandex.volkov.java_kanban.task.Epic;
import com.yandex.volkov.java_kanban.task.Status;
import com.yandex.volkov.java_kanban.task.Subtask;
import com.yandex.volkov.java_kanban.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest extends TaskManagerTest<TaskManager> {
    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;
    private static HttpClient client;
    private static String urlHttpTaskServer;
    private Task task;
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;

    private static Gson gson;

    @BeforeAll
    public static void initGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();
    }

    @BeforeEach
    void startServersAndInitTasks() {
        try {
            kvServer = new KVServer();
            kvServer.start();

            manager = Manager.getDefault();

            httpTaskServer = new HttpTaskServer(manager);
            httpTaskServer.start();

            urlHttpTaskServer ="http://localhost:8080/";

            client = HttpClient.newHttpClient();
        } catch (IOException e) {
            throw new HttpException("Ошибка при запуске сервера");
        }

        task = new Task("Title Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 2, 1, 10, 0, 0), 10);

        epic = new Epic("Title Epic", "Descr Epic", Status.NEW,
                LocalDateTime.of(2023, 12, 1, 8, 0, 0), 60);

        subtask1 = new Subtask("Title subtask 1", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0), 60);
        subtask2 = new Subtask("Title subtask 2", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 13, 0, 0), 60);
    }

    @AfterEach
    void stopServersAfterEach() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    private void addTasks() {
        manager.addNewTask(task);
        manager.addNewEpic(epic);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        epic.getSubtaskId().add(subtask1.getId());
        epic.getSubtaskId().add(subtask2.getId());
    }

    private HttpResponse<String> sendGetRequest(String path) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(urlHttpTaskServer.concat(path));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Ошибка при выполнении GET-запроса.");
        }
        return response;
    }

    private HttpResponse<String> sendPostRequest(String path, Task newTask) {
        try {
            URI uri = URI.create(urlHttpTaskServer.concat(path));
            String json = gson.toJson(newTask);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(body)
                    .build();
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Ошибка при выполнении POST-запроса.");
        }
    }

    private void sendDeleteRequest(String path) {
        URI uri = URI.create(urlHttpTaskServer.concat(path));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Ошибка при выполнении DELETE-запроса.");
        }
    }


    @Test
    void getRequest_emptyResponse_prioritizedInManagerIsEmpty() {
        assertEquals("[]", sendGetRequest("tasks").body());
    }

    @Test
    public void getRequest_emptyResponse_historyInManagerIsEmpty() {
        assertEquals("[]", sendGetRequest("tasks/history").body());
    }

    @Test
    void getRequest_emptyResponse_tasksInManagerAreNotExist() {
        assertEquals("[]", sendGetRequest("tasks/task").body());
    }

    @Test
    void getRequest_emptyResponse_epicsInManagerAreNotExist() {
        assertEquals("[]", sendGetRequest("tasks/epic").body());
    }

    @Test
    void getRequest_emptyResponse_subtasksInManagerAreNotExist() {
        assertEquals("[]", sendGetRequest("tasks/subtask").body());
    }

    @Test
    public void getResponse_responseListWithAllTasks_tasksInManagerAreExist() {
        addTasks();
        assertEquals(gson.toJson(manager.getAllTasks()), sendGetRequest("tasks/task").body());
    }

    @Test
    public void getResponse_responseListWithAllSubtasks_subtasksInManagerAreExist() {
        addTasks();
        assertEquals(gson.toJson(manager.getAllSubtasks()), sendGetRequest("tasks/subtask").body());
    }

    @Test
    public void getResponse_responseListWithAllEpics_epicsInManagerAreExist() {
        addTasks();
        assertEquals(gson.toJson(manager.getAllEpics()), sendGetRequest("tasks/epic").body());
    }

    @Test
    public void getResponse_responseListWithSubtaskInEpicByIdEpic_epicWithSubtasksInManagerAreExist() {
        addTasks();
        assertEquals(gson.toJson(manager.getAllSubtaskInEpic(epic.getId())),
                sendGetRequest("tasks/subtask/epic/?id=" + epic.getId()).body());
    }

    @Test
    void postRequest_status200Response_createTask() {
        assertEquals(HttpURLConnection.HTTP_OK, sendPostRequest("tasks/task", task).statusCode());
    }

    @Test
    void postRequest_status200Response_createEpic() {
        assertEquals(HttpURLConnection.HTTP_OK, sendPostRequest("tasks/epic", epic).statusCode());
    }

    @Test
    void postRequest_status200Response_createSubtask() {
        assertEquals(HttpURLConnection.HTTP_OK, sendPostRequest("tasks/subtask", subtask1).statusCode());
    }

    @Test
    void getRequest_correctTaskInResponse_correctIdTaskInRequest() {
        addTasks();
        assertEquals(gson.toJson(task), sendGetRequest("tasks/task/?id=" + task.getId()).body());
    }

    @Test
    void getRequest_emptyResponse_incorrectIdTaskInRequest() {
        assertEquals("", sendGetRequest("tasks/task/?id=2").body());
    }


    @Test
    void getRequest_correctEpicInResponse_correctIdEpicInRequest() {
        addTasks();
        assertEquals(gson.toJson(epic), sendGetRequest("tasks/epic/?id=" + epic.getId()).body());
    }

    @Test
    void getRequest_emptyResponse_incorrectIdEpicInRequest() {
        addTasks();
        assertEquals("", sendGetRequest("tasks/epic/?id=10").body());
    }

    @Test
    void getRequest_correctSubtaskInResponse_incorrectIdSubtaskInRequest() {
        addTasks();
        assertEquals(gson.toJson(subtask1), sendGetRequest("tasks/subtask/?id=" + subtask1.getId()).body());
    }

    @Test
    void getRequest_emptyResponse_incorrectIdSubtaskInRequest() {
        addTasks();
        assertEquals("", sendGetRequest("tasks/subtask/?id=8").body());
    }

    @Test
    public void getRequest_emptyResponse_deleteTaskByIdAndGetDeletedTask() {
        addTasks();
        sendDeleteRequest("tasks/task/?id=1");
        assertEquals("", sendGetRequest("tasks/task/?id=1").body());
    }

    @Test
    public void getRequest_emptyResponse_deleteEpicByIdAndGetDeletedEpic() {
        addTasks();
        sendDeleteRequest("tasks/epic/?id=1");
        assertEquals("", sendGetRequest("tasks/epic/?id=1").body());
    }

    @Test
    public void getRequest_emptyResponse_deleteSubtaskByIdAndGetDeletedSubtask() {
        addTasks();
        sendDeleteRequest("tasks/subtask/?id=1");
        assertEquals("", sendGetRequest("tasks/subtask/?id=1").body());
    }

    @Test
    public void getRequest_emptyResponse_deleteAllTaskAndGetAllTask() {
        addTasks();
        sendDeleteRequest("tasks/task/");
        assertEquals("[]", sendGetRequest("tasks/task").body());
    }

    @Test
    public void getRequest_emptyResponse_deleteAllEpicAndGetAllEpic() {
        addTasks();
        sendDeleteRequest("tasks/epic/");
        assertEquals("[]", sendGetRequest("tasks/epic").body());
    }

    @Test
    public void getRequest_emptyResponse_deleteAllSubtaskAndGetAllSubtask() {
        addTasks();
        sendDeleteRequest("tasks/subtask/");
        assertEquals("[]", sendGetRequest("tasks/subtask").body());
    }

    @Test
    public void getResponse_responseWithUpdatedTask_updateTaskById() {
        addTasks();
        Task newTask = new Task("Updated Task", "Description Task", Status.NEW,
                LocalDateTime.of(2023, 2, 1, 10, 0, 0), 15);
        newTask.setId(task.getId());
        sendPostRequest("tasks/task/?id=" + task.getId(), newTask);
        assertEquals(gson.toJson(newTask), sendGetRequest("tasks/task/?id=" + task.getId()).body());
    }

    @Test
    public void getResponse_responseWithUpdatedSubtask_updateSubtaskById() {
        addTasks();
        Subtask newSubtaskFirst = new Subtask("Updated Subtask", "Descr subtask", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0), 60);
        newSubtaskFirst.setId(subtask1.getId());
        newSubtaskFirst.setEpicId(subtask1.getEpicId());
        sendPostRequest("tasks/subtask/?id=" + subtask1.getId(), newSubtaskFirst);
        assertEquals(gson.toJson(newSubtaskFirst), sendGetRequest("tasks/subtask/?id=" + subtask1.getId()).body());
    }

    @Test
    public void getResponse_responseWithUpdatedSubtask_updateSubtaskById1() {
        addTasks();
        Subtask newSubtaskFirst = new Subtask("Updated Subtask", "Descr subtask", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 1, 1, 10, 0, 0), 60);
        newSubtaskFirst.setId(subtask1.getId());
        newSubtaskFirst.setEpicId(subtask1.getEpicId());
        sendPostRequest("tasks/subtask/?id=" + subtask1.getId(), newSubtaskFirst);
        assertEquals(gson.toJson(epic), sendGetRequest("tasks/epic/?id=" + epic.getId()).body());
    }
}