package com.example.backend.serviceImpl;

import com.example.backend.dto.response.BlogResponse;
import com.example.backend.dto.response.NotificationResponse;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Blog;
import com.example.backend.model.Comment;
import com.example.backend.model.Notification;
import com.example.backend.model.User;
import com.example.backend.patterns.observer.Observer;
import com.example.backend.repository.BlogRepository;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.NotificationRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotifyService implements com.example.backend.service.NotifyService, Observer {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Override
    public void update(String message) {
        List<String> paths = new ArrayList<>(Arrays.asList(message.split(" ")));
        User commentOwner = userRepository.findByusername(paths.get(0));

        Blog blog = blogRepository.findById(paths.get(paths.size() - 1)).orElseThrow(
                () -> new NotFoundException("Blog is not found with id: " + paths.get(paths.size() - 1))
        );

        //Notify to own blog
        String ownBlogId = blog.getUserId();
        Notification ownBlogNotification = new Notification();
        ownBlogNotification.setContent(message);
        ownBlogNotification.setUserId(ownBlogId);
        ownBlogNotification.setLocalDateTime(LocalDateTime.now());
        notificationRepository.save(ownBlogNotification);

        //Notify to people who have commented blog
        List<Comment> comments = commentRepository.findByblogId(paths.get(paths.size() - 1));
        Set<String> sentUserIds = new HashSet<>();

        Set<String> targetUserIds = comments.stream()
                .map(Comment::getUserId)
                .filter(id -> !commentOwner.getId().equals(id) && !ownBlogId.equals(id))
                .collect(Collectors.toSet());

        List<User> users = userRepository.findAllById(targetUserIds);

        for (User user : users) {
            Notification notification = new Notification();
            notification.setContent(message);
            notification.setUserId(user.getId());
            notification.setLocalDateTime(LocalDateTime.now());
            notificationRepository.save(notification);
        }

    }

    @Override
    public void delete(String blogId) {
        List<Notification> notifications = notificationRepository.findAll();
        for(Notification notification : notifications){
            List<String> paths = new ArrayList<>(Arrays.asList(notification.getContent().split(" ")));
            if(paths.get(paths.size() -1).equals(blogId)){
                notificationRepository.deleteById(notification.getId());
            }
        }
    }

    @Override
    public List<NotificationResponse> getAllNotification(String userId) {
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findByuserId(userId);
        Set<String> blogIds = new HashSet<>();

        for (Notification notification : notifications) {
            List<String> paths = new ArrayList<>(Arrays.asList(notification.getContent().split(" ")));
            if (!paths.isEmpty()) {
                blogIds.add(paths.get(paths.size() - 1));
            }
        }

        List<Blog> blogs = blogRepository.findAllById(blogIds);
        Map<String, Blog> blogMap = blogs.stream()
                .collect(Collectors.toMap(Blog::getId, blog -> blog));

        for (Notification notification : notifications)
        {
            List<String> paths = new ArrayList<>(Arrays.asList(notification.getContent().split(" ")));
            String blogId = paths.get(paths.size() - 1);
            Blog blog = blogMap.get(blogId);
            if (blog == null) {
                throw new NotFoundException("Blog is not found with id: " + blogId);
            }

            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setId(notification.getId());
            notificationResponse.setLocalDateTime(notification.getLocalDateTime());
            notificationResponse.setUserId(notification.getUserId());
            String content = String.join(paths.get(0) + " " + paths.get(1) + " " + paths.get(2) + " " + blog.getContent());
            notificationResponse.setContent(content);

            notificationResponses.add(notificationResponse);
        }

        return notificationResponses;
    }
}
