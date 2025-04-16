package com.example.backend.service;

import com.example.backend.dto.response.NotificationResponse;
import com.example.backend.model.Notification;

import java.util.List;

public interface NotifyService {
    List<NotificationResponse> getAllNotification(String userId);
}
