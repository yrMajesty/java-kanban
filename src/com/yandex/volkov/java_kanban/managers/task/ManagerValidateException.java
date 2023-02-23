package com.yandex.volkov.java_kanban.managers.task;

public class ManagerValidateException extends RuntimeException{
    public ManagerValidateException(String message) {
        super(message);
    }
}