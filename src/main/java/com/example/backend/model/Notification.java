package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(
        "notifications"
)
@Data
public class Notification {
    @Id
    private String id;
    private String content;
    private LocalDateTime localDateTime;
    private String userId;
}
