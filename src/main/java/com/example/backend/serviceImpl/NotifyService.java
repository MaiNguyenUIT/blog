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
        User existUser = userRepository.findByusername(paths.get(0));

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
        for(Comment comment : comments){
            String userId = comment.getUserId();

            if (!sentUserIds.contains(userId) && !existUser.getId().equals(userId) && !ownBlogId.equals(userId)) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    Notification notification = new Notification();
                    notification.setContent(message);
                    notification.setUserId(user.getId());
                    notification.setLocalDateTime(LocalDateTime.now());
                    notificationRepository.save(notification);

                    sentUserIds.add(userId);
                }
            }
        }

    }

    @Override
    public List<NotificationResponse> getAllNotification(String userId) {
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        for (Notification notification : notificationRepository.findByuserId(userId))
        {
            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setId(notification.getId());
            notificationResponse.setLocalDateTime(notification.getLocalDateTime());
            notificationResponse.setUserId(notification.getUserId());

            List<String> paths = new ArrayList<>(Arrays.asList(notification.getContent().split(" ")));
            String content = new String(paths.get(0) + " " + paths.get(1) + " " + paths.get(2) + " " + paths.get(3));
            notificationResponse.setContent(content);

            notificationResponses.add(notificationResponse);
        }

        return notificationResponses;
    }
}
