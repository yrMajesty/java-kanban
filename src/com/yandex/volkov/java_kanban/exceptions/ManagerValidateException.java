package com.yandex.volkov.java_kanban.exceptions;

public class ManagerValidateException extends RuntimeException{
    public ManagerValidateException(String message) {
        super(message);
    }
}