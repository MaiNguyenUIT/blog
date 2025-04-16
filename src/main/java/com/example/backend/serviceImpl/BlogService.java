package com.example.backend.serviceImpl;

import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.response.BlogResponse;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.BlogMapper;
import com.example.backend.model.Blog;
import com.example.backend.model.User;
import com.example.backend.repository.BlogRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BlogService implements com.example.backend.service.BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<Blog> getUserBlog(String userId) {
        return blogRepository.findByuserId(userId);
    }

    @Override
    public List<BlogResponse> getALlBlog() {
        List<BlogResponse> blogResponses = new ArrayList<>();

        for(Blog blog : blogRepository.findAll()) {
            User user = userRepository.findById(blog.getUserId()).orElse(null);
            BlogResponse blogResponse = new BlogResponse();
            String avatar = "";
            String userName = "";
            if(user != null){
                avatar = user.getAvatarUrl();
                userName = user.getUsername();
            } else {
                avatar = "https://th.bing.com/th/id/OIP.HeegDxfGGg2f1BCm3xxsVgHaDw?rs=1&pid=ImgDetMain";
                userName = "Anonymous";
            }


            blogResponse.setContent(blog.getContent());
            blogResponse.setTitle(blog.getTitle());
            blogResponse.setUserAvatar(avatar);
            blogResponse.setUserName(userName);
            blogResponse.setCreatedDate(blog.getCreatedDate());
            blogResponse.setId(blog.getId());
            blogResponse.setPublic(blog.isPublic());

            blogResponses.add(blogResponse);
        }
        return blogResponses;
    }

    @Override
    public List<BlogResponse> getAllPublicBlog() {
        List<BlogResponse> blogResponses = new ArrayList<>();
        for(Blog blog : blogRepository.findByIsPublicTrue()) {
            User user = userRepository.findById(blog.getUserId()).orElseThrow(
                    () -> new NotFoundException("User not found with id: " + blog.getUserId())
            );
            String avatar = user.getAvatarUrl();
            String userName = user.getUsername();

            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setContent(blog.getContent());
            blogResponse.setTitle(blog.getTitle());
            blogResponse.setUserAvatar(avatar);
            blogResponse.setUserName(userName);
            blogResponse.setCreatedDate(blog.getCreatedDate());
            blogResponse.setId(blog.getId());

            blogResponses.add(blogResponse);
        }
        return blogResponses;
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

    @Override
    public void publicBlogs(List<String> blogIds, boolean isPublic) {
        if(isPublic)
        {
            for(String id : blogIds){
                Blog blog = blogRepository.findById(id).orElseThrow(
                        () ->  new NotFoundException("Blog is not found with id: " + id)
                );
                blog.setPublic(true);
                blogRepository.save(blog);
            }
        } else {
            for(String id : blogIds){
                Blog blog = blogRepository.findById(id).orElseThrow(
                        () ->  new NotFoundException("Blog is not found with id: " + id)
                );
                blog.setPublic(false);
                blogRepository.save(blog);
            }
        }
    }
}
