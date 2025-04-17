package com.example.backend.dto.request;

import com.example.backend.ENUM.Role;
import lombok.Data;

@Data
public class UpdateInforRequest {
    private String email;
    private Role userRole;
}
