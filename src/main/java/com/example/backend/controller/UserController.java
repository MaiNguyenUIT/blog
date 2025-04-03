package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.UpdateInforRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.model.User;
import com.example.backend.serviceImpl.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<User>>> getAllUser(@RequestHeader("Authorization") String jwt){
        List<User> users = userService.getAllUser();
        ApiResponse<List<User>> apiResponse = ApiResponse.<List<User>>builder()
                .status(200)
                .data(users)
                .message("Login successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUserInfor(@RequestHeader("Authorization") String jwt, @PathVariable String userId, @RequestBody UpdateInforRequest updateInforRequest){
        User user = userService.updateUserInfor(userId, updateInforRequest);
        ApiResponse<User> apiResponse = ApiResponse.<User>builder()
                .status(200)
                .data(user)
                .message("Update user information successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestHeader("Authorization") String jwt, @PathVariable String userId){
        userService.deleteUser(userId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(200)
                .message("Delete user successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
