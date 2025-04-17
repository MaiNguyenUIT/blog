package com.example.backend.service;

import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.response.BlogResponse;
import com.example.backend.model.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getUserBlog(String userId);
    List<BlogResponse> getALlBlog();
    List<BlogResponse> getAllPublicBlog();
    BlogDTO createBlog(String userId, BlogDTO blogDTO);
    BlogDTO updateBlog(String userId, String blogId, BlogDTO blogDTO);
    Blog getBlog(String blogId);
    List<Blog> getBlogsSortedByPriority();
    void deleteBlog(String id);
    void updateBlogStatus(List<Blog> blogs, String isPublic);
}
