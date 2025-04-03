package com.example.backend.serviceImpl;

import com.example.backend.dto.CommentDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.CommentMapper;
import com.example.backend.model.Blog;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.repository.BlogRepository;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements com.example.backend.service.CommentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CommentRepository commentRepository;
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
        return commentDTO;
    }

    @Override
    public List<Comment> getBlogComment(String blogId) {
        return commentRepository.findByblogId(blogId);
    }

    @Override
    public void deleteComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new NotFoundException("Comment is not exist with id: " + commentId));
        if(!comment.getUserId().equals(userId)){
            throw new BadRequestException("U can not delete other people's comment");
        }
        commentRepository.deleteById(commentId);
    }
}
