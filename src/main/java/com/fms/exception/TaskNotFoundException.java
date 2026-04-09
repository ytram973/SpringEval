package com.fms.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Tâche introuvable avec l'id : " + id);
    }
}