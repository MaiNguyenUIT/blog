package com.example.backend.dto.request;

import com.example.backend.ENUM.USER_ROLE;
import lombok.Data;

@Data
public class UpdateInforRequest {
    private String name;
    private String password;
    private USER_ROLE userRole;
    private String avatarUrl;
}
