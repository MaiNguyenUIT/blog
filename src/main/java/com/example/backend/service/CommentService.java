package com.example.backend.service;

import com.example.backend.ENUM.USER_ROLE;
import com.example.backend.dto.CommentDTO;
import com.example.backend.model.Comment;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(String userId, String blogId, CommentDTO commentDTO);
    List<Comment> getBlogComment(String blogId);
    void deleteComment(String commentId, String userId, USER_ROLE userRole);
}
