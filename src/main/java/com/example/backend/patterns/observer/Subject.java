package com.example.backend.patterns.observer;

public interface Subject {
    void registerUser(Observer customer);
    void removeUser(Observer customer);
    void notifyUsers(String message);
}
