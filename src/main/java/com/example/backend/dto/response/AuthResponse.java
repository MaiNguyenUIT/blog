package com.example.backend.dto.response;

import com.example.backend.ENUM.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
}
