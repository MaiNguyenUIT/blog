package com.example.backend.service;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse signIn(LoginRequest loginRequest);
    void signUp(RegisterRequest userAccountDTO);
}
