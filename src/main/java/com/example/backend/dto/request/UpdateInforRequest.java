package com.example.backend.dto.request;

import com.example.backend.ENUM.Role;
import lombok.Data;

@Data
public class UpdateInforRequest {
    private String name;
    private String password;
    private Role userRole;
    private String avatarUrl;
}
