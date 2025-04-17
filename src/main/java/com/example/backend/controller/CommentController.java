package com.example.backend.controller;

import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.CommentDTO;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CommentService;
import com.example.backend.service.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;


    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN') || @blogSecurity.isOwner(#blogId)")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable String commentId){
        User user = userService.findUserFromToken();
        commentService.deleteComment(commentId, user.getId(), user.getUserRole());
        return ResponseEntity.ok(Collections.singletonMap("message", "Delete comment successfully"));
    }
}
