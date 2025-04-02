package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(
        "comments"
)
@Data
public class Comment {
    @Id
    private String id;
    private String content;
    private String userId;
    private String blogId;
    private LocalDateTime createdDate = LocalDateTime.now();
}
