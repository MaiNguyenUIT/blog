package com.example.backend.service;

import com.example.backend.dto.BlogDTO;
import com.example.backend.model.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getUserBlog(String userId);
    List<Blog> getALlBlog();
    BlogDTO createBlog(String userId, BlogDTO blogDTO);
    BlogDTO updateBlog(String userId, String blogId, BlogDTO blogDTO);
    Blog getBlog(String blogId);
    List<Blog> getBlogsSortedByPriority();
    void deleteBlog(String id);
}
