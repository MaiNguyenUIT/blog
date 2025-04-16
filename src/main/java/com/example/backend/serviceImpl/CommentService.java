package com.example.backend.serviceImpl;

import com.example.backend.ENUM.Role;
import com.example.backend.dto.CommentDTO;
import com.example.backend.dto.response.CommentResponse;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.CommentMapper;
import com.example.backend.model.Blog;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.patterns.observer.Observer;
import com.example.backend.patterns.observer.Subject;
import com.example.backend.repository.BlogRepository;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService implements com.example.backend.service.CommentService, Subject {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NotifyService notifyService;

    List<Observer> observers = new ArrayList<>();

    @Override
    public CommentDTO createComment(String userId, String blogId, CommentDTO commentDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found with id: " + userId));

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("Blog is not found with id: " + blogId));
        Comment comment = CommentMapper.INSTANCE.toEntity(commentDTO);
        comment.setBlogId(blogId);
        comment.setUserId(userId);
        commentRepository.save(comment);

        registerUser(notifyService);

        notifyUsers(user.getUsername() + " commented in " + blog.getContent() + " " + blogId);
        return commentDTO;
    }

    @Override
    public List<CommentResponse> getBlogComment(String blogId) {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(Comment comment : commentRepository.findByblogId(blogId)){
            User user = userRepository.findById(comment.getUserId()).orElse(null);
            String userName = "";
            if(user != null){
                userName = user.getUsername();
            } else {
                userName = "Anonymous";
            }
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setId(comment.getId());
            commentResponse.setContent(comment.getContent());
            commentResponse.setUserName(userName);
            commentResponse.setCreatedDate(commentResponse.getCreatedDate());
            commentResponses.add(commentResponse);
        }

        return commentResponses;
    }

    @Override
    public void deleteComment(String commentId, String userId, Role userRole) {
        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new NotFoundException("Comment is not exist with id: " + commentId));
        if(!comment.getUserId().equals(userId) && userRole.equals(Role.ROLE_USER)){
            throw new BadRequestException("U can not delete other people's comment");
        }
        commentRepository.deleteById(commentId);
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
        for(Observer observer : observers){
            observer.update(message);
        }
    }
}
