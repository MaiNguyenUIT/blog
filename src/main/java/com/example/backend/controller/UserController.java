package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.UpdateInforRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.model.User;
import com.example.backend.serviceImpl.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUserInfor(@PathVariable String userId, @RequestBody UpdateInforRequest updateInforRequest){
        User user = userService.updateUserInfor(userId, updateInforRequest);
        ApiResponse<User> apiResponse = ApiResponse.<User>builder()
                .data(user)
                .message("Update user information successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete user successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/avatar")
    public ResponseEntity<ApiResponse<User>> uploadImage(@RequestParam("avatar") MultipartFile file) throws IOException {
        User user = userService.findUserFromToken();
        User updatedUser = userService.uploadImage(user, file);
        ApiResponse<User> apiResponse = ApiResponse.<User>builder()
                .data(updatedUser)
                .message("Upload user avatar successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserInformation() throws IOException {
        User user = userService.findUserFromToken();
        return ResponseEntity.ok(user);
    }
}
