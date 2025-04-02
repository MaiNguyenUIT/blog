package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(
        "blogs"
)
@Data
public class Blog {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdDate = LocalDateTime.now();
    private String userId;
    private int priority;
}
