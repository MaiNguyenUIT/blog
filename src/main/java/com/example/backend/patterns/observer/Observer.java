package com.example.backend.patterns.observer;

public interface Observer {
    void update(String message);
    void delete(String blogId);
}
