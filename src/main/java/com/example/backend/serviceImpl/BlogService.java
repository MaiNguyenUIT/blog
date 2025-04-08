package com.example.backend.serviceImpl;

import com.example.backend.dto.BlogDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.BlogMapper;
import com.example.backend.model.Blog;
import com.example.backend.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BlogService implements com.example.backend.service.BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Override
    public List<Blog> getUserBlog(String userId) {
        return blogRepository.findByuserId(userId);
    }

    @Override
    public List<Blog> getALlBlog() {
        return blogRepository.findAll();
    }

    @Override
    public BlogDTO createBlog(String userId, BlogDTO blogDTO) {
        Blog blog = BlogMapper.INSTANCE.toEntity(blogDTO);
        blog.setUserId(userId);
        blogRepository.save(blog);
        return blogDTO;
    }

    @Override
    public BlogDTO updateBlog(String userId, String blogId, BlogDTO blogDTO) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("Blog is not found with id: " + blogId));
        if(!blog.getUserId().equals(userId)){
            throw new BadRequestException("You can't update other people's blog");
        }

        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        blogRepository.save(blog);
        return blogDTO;
    }

    @Override
    public Blog getBlog(String blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("Blog is not found with id: " + blogId));
    }

    @Override
    public List<Blog> getBlogsSortedByPriority() {
        return blogRepository.findAll(Sort.by(Sort.Order.desc("createdDate")));
    }

    @Override
    public void deleteBlog(String id) {
        blogRepository.deleteById(id);
    }
}
