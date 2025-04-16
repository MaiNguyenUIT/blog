package com.example.backend.dto.response;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Data
public class CommentResponse {
    private String id;
    private String content;
    private String userName;
    private String userAvatar;
    private LocalDateTime createdDate;
}
