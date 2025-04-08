package com.example.backend.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Data
public class BlogDTO {
    private String title;
    private String content;
}
