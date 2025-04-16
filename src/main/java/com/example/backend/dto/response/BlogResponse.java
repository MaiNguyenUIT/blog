package com.example.backend.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogResponse {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdDate = LocalDateTime.now();
    private String userName;
    private String userAvatar;
    private boolean isPublic = false;
}
