package com.example.backend.serviceImpl;

import com.example.backend.dto.BlogDTO;
import com.example.backend.model.Blog;
import com.example.backend.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class BlogService implements com.example.backend.service.BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Override
    public List<Blog> getUserBlog(String userId) {
        List<Blog> blogs = blogRepository.findByuserId(userId);
        return blogs;
    }

    @Override
    public List<Blog> getALlBlog() {
        return blogRepository.findAll();
    }

    @Override
    public Blog createBlog(String userId, BlogDTO blogDTO) {
        return null;
    }

    @Override
    public Blog updateBlog(String userId, BlogDTO blogDTO) {
        return null;
    }

    @Override
    public Blog getBlog(String blogId) {
        return null;
    }

    @Override
    public void deleteBlog(String id) {

    }
}
