package com.example.backend.controller;

import com.example.backend.dto.response.NotificationResponse;
import com.example.backend.model.Notification;
import com.example.backend.model.User;
import com.example.backend.serviceImpl.NotifyService;
import com.example.backend.serviceImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/notification")
public class NotificationController {
    @Autowired
    private UserService userService;
    @Autowired
    private NotifyService notifyService;
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<List<NotificationResponse>> getUserNotification(){
        User user = userService.findUserFromToken();
        List<NotificationResponse> notifications = notifyService.getAllNotification(user.getId());
        return ResponseEntity.ok(notifications);
    }
}
