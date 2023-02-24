package com.yandex.volkov.java_kanban.exceptions;

public class HttpException extends RuntimeException{
    public HttpException(String message) {
        super(message);
    }
}
