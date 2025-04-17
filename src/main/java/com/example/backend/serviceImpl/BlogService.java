package com.example.backend.serviceImpl;

import com.example.backend.ENUM.Role;
import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.UserDataDTO;
import com.example.backend.dto.response.BlogResponse;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.BlogMapper;
import com.example.backend.model.Blog;
import com.example.backend.model.User;
import com.example.backend.patterns.observer.Observer;
import com.example.backend.patterns.observer.Subject;
import com.example.backend.repository.BlogRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogService implements com.example.backend.service.BlogService, Subject {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotifyService notifyService;
    List<Observer> observers = new ArrayList<>();
    @Override
    public List<Blog> getUserBlog(String userId) {
        return blogRepository.findByuserId(userId);
    }

    @Override
    public List<BlogResponse> getALlBlog() {
        List<BlogResponse> blogResponses = new ArrayList<>();

        List<Blog> blogs = blogRepository.findAll();

        Set<String> userIds = blogs.stream()
                .map(Blog::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<User> users = userRepository.findAllById(userIds);
        Map<String, UserDataDTO> userIdToData = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> {
                            UserDataDTO data = new UserDataDTO();
                            data.setUserName(user.getUsername());
                            data.setAvatarUrl(user.getAvatarUrl());
                            return data;
                        }
                ));

        for(Blog blog : blogs) {
            UserDataDTO userData = userIdToData.get(blog.getUserId());
            String userName = "Anonymous";
            String avatar = "https://th.bing.com/th/id/OIP.HeegDxfGGg2f1BCm3xxsVgHaDw?rs=1&pid=ImgDetMain";

            if (userData != null) {
                userName = userData.getUserName();
                avatar = userData.getAvatarUrl();
            }

            BlogResponse blogResponse = new BlogResponse();
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

        List<Blog> blogs = blogRepository.findByIsPublicTrue();

        Set<String> userIds = blogs.stream()
                .map(Blog::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<User> users = userRepository.findAllById(userIds);
        Map<String, UserDataDTO> userIdToData = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> {
                            UserDataDTO data = new UserDataDTO();
                            data.setUserName(user.getUsername());
                            data.setAvatarUrl(user.getAvatarUrl());
                            return data;
                        }
                ));

        for(Blog blog : blogs) {
            UserDataDTO userData = userIdToData.get(blog.getUserId());

            String userName = "Anonymous";
            String avatar = "https://th.bing.com/th/id/OIP.HeegDxfGGg2f1BCm3xxsVgHaDw?rs=1&pid=ImgDetMain";

            if (userData != null) {
                userName = userData.getUserName();
                avatar = userData.getAvatarUrl();
            }

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
        User user = userRepository.findById(userId).
                orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        blog.setPublic(blogDTO.isPublic());
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
        notifyUsers(id);
    }

    @Override
    public void updateBlogStatus(List<Blog> blogs, String isPublic) {
        if(isPublic.equals("true")){
            for(Blog blog : blogs){
                blog.setPublic(true);
                blogRepository.save(blog);
            }
        } else {
            for(Blog blog : blogs){
                blog.setPublic(false);
                blogRepository.save(blog);
            }
        }
    }

    @Override
    public void registerUser(Observer customer) {
        observers.add(customer);
    }

    @Override
    public void removeUser(Observer customer) {

    }

    @Override
    public void notifyUsers(String message) {
        registerUser(notifyService);
        for(Observer observer : observers){
            observer.delete(message);
        }
    }
}
