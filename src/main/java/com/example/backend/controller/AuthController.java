package com.example.backend.controller;

import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.model.User;
import com.example.backend.serviceImpl.AuthService;
import com.example.backend.serviceImpl.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> signIn(@RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = authService.signIn(loginRequest);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .message("Login successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody RegisterRequest registerRequest){
        authService.signUp(registerRequest);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Register successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
