package com.yandex.volkov.java_kanban.web;


import com.yandex.volkov.java_kanban.exceptions.HttpException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final String apiToken;
    private final String urlDataTokenServer;


    public KVTaskClient(String urlKVServer) {
        this.client = HttpClient.newHttpClient();
        this.urlDataTokenServer = urlKVServer;
        this.apiToken = registration();
    }

    private String registration(){
        String uriRegister = urlDataTokenServer.concat("register/");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriRegister))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        return sendRequest(request).body();
    }

    public void put(String value, String json) {
        String uriSave = urlDataTokenServer.concat("save/").concat(value).concat("?API_TOKEN=").concat(apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(uriSave))
                .build();
        sendRequest(request);
    }

    public String load(String value) {
        String uriLoad = urlDataTokenServer.concat("load/").concat(value).concat("?API_TOKEN=").concat(apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriLoad))
                .build();
        return sendRequest(request).body();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Ошибка выполнения запроса");
        }
    }
}