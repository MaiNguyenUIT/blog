package com.example.backend.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private String id;
    private String content;
    private LocalDateTime localDateTime;
    private String userId;
}
