package com.yandex.volkov.java_kanban.exceptions;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }
    public ManagerSaveException(String message, IOException e) {
        super(message);
    }
}
