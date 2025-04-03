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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> createComment(@RequestHeader("Authorization") String jwt,
                                                                 @RequestParam String commentId){
        User user = userService.findUserByJwtToken(jwt);
        commentService.deleteComment(commentId, user.getId());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(200)
                .message("Delete blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
