package com.example.backend.security;

import com.example.backend.ENUM.Role;
import com.example.backend.model.Blog;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.repository.BlogRepository;
import com.example.backend.repository.CommentRepository;
import com.example.backend.serviceImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("commentSecurity")
public class CommentSecurity {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    public boolean isOwnerOrAdmin(String stringId){
        User user = userService.findUserFromToken();

        if(user.getUserRole().equals(Role.ROLE_ADMIN)){
            return true;
        }

        Comment comment = commentRepository.findById(stringId).orElse(null);
        if (comment == null) return false;

        return comment.getUserId().equals(user.getId());
    }
}
