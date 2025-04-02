package com.example.backend.service;

import com.example.backend.dto.BlogDTO;
import com.example.backend.model.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getUserBlog(String userId);
    List<Blog> getALlBlog();
    Blog createBlog(String userId, BlogDTO blogDTO);
    Blog updateBlog(String userId, BlogDTO blogDTO);
    Blog getBlog(String blogId);
    void deleteBlog(String id);
}
