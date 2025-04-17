package com.example.backend.model;

import com.example.backend.ENUM.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
        "users"
)
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private Role userRole = Role.ROLE_USER;
    private String avatarUrl;
}
