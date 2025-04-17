package com.example.backend.security;

import com.example.backend.ENUM.Role;
import com.example.backend.model.Blog;
import com.example.backend.model.User;
import com.example.backend.repository.BlogRepository;
import com.example.backend.serviceImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component("blogSecurity")
public class BlogSecurity {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserService userService;
    public boolean isOwner(String blogId){
        User user = userService.findUserFromToken();

        Blog blog = blogRepository.findById(blogId).orElse(null);
        if (blog == null) return false;

        return blog.getUserId().equals(user.getId());
    }
}
