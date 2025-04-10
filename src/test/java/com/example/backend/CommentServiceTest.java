package com.example.backend;

import com.example.backend.ENUM.Role;
import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.CommentDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.BlogMapper;
import com.example.backend.mapper.CommentMapper;
import com.example.backend.model.Blog;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.repository.BlogRepository;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.serviceImpl.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment(){
        String userId = "testUserId";
        String blogId = "testBlogId";

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("testContent");

        Comment comment = CommentMapper.INSTANCE.toEntity(commentDTO);
        comment.setUserId(userId);
        comment.setBlogId(blogId);

        User user = new User();
        user.setId(userId);

        Blog blog = new Blog();
        blog.setId(blogId);

        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        CommentDTO result = commentService.createComment(userId, blogId, commentDTO);

        assertNotNull(result);
        assertEquals(comment.getContent(), result.getContent());

        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    void testGetBlogComment(){
        String userId1 = "testUserId1";
        String userId2 = "testUserId2";
        String blogId = "testBlogId";

        Comment comment1 = new Comment();
        comment1.setUserId(userId1);
        comment1.setBlogId(blogId);
        comment1.setContent("testContent1");

        Comment comment2 = new Comment();
        comment2.setUserId(userId2);
        comment2.setBlogId(blogId);
        comment2.setContent("testContent2");

        List<Comment> comments = new ArrayList<>();
        comments.add(comment2);
        comments.add(comment1);

        Mockito.when(commentRepository.findByblogId(blogId)).thenReturn(comments);

        List<Comment> result = commentService.getBlogComment(blogId);

        assertNotNull(result);
        assertEquals(comments.size(), result.size());
        assertEquals(comments.get(0), result.get(0));
        assertEquals(comments.get(1), result.get(1));

        Mockito.verify(commentRepository, Mockito.times(1)).findByblogId(blogId);
    }

    @Test
    void testDeleteComment(){
        String userId = "testUserId";
        String adminId = "testAdminId";
        String blogId = "testBlogId";
        String commentId1 = "testCommentId1";
        String commentId2 = "testCommentId2";

        User user = new User();
        user.setId(userId);
        user.setUserRole(Role.ROLE_USER);

        User admin = new User();
        admin.setId(adminId);
        admin.setUserRole(Role.ROLE_ADMIN);


        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setBlogId(blogId);
        comment.setContent("testContent");
        comment.setId(commentId1);

        Comment comment2 = new Comment();
        comment2.setUserId(userId);
        comment2.setBlogId(blogId);
        comment2.setContent("testContent2");
        comment2.setId(commentId2);

        Mockito.when(commentRepository.findById(commentId1)).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.findById(commentId2)).thenReturn(Optional.of(comment2));
        Mockito.doNothing().when(commentRepository).deleteById(comment.getId());
        Mockito.doNothing().when(commentRepository).deleteById(comment2.getId());

        commentService.deleteComment(commentId1, user.getId(), user.getUserRole());
        commentService.deleteComment(commentId2, admin.getId(), admin.getUserRole());
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(commentId1);
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(commentId2);
    }

    @Test
    void testCreateComment_UserNotFound(){
        String blogId = "blogId";
        String userId = "nonExistUser";

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        CommentDTO commentDTO = new CommentDTO();

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            commentService.createComment(userId, blogId, commentDTO);
        });

        assertTrue(ex.getMessage().contains("User is not found with id: " + userId));
    }

    @Test
    void testCreateComment_BlogNotFound(){
        String blogId = "NonExistBlog";
        String userId = "userId";
        User user = new User();
        user.setId(userId);

        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        CommentDTO commentDTO = new CommentDTO();

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            commentService.createComment(userId, blogId, commentDTO);
        });

        assertTrue(ex.getMessage().contains("Blog is not found with id: " + blogId));
    }

    @Test
    void testDeleteComment_CommentNotFound(){
        String commentId = "nonExistComment";
        String userId = "user1";
        User user = new User();
        user.setId(userId);
        user.setUserRole(Role.ROLE_USER);

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            commentService.deleteComment(commentId, userId, user.getUserRole());
        });

        assertTrue(ex.getMessage().contains("Comment is not exist with id: " + commentId));
    }

    @Test
    void testDeleteComment_CannotDelete(){
        String commentId = "commentId";
        String userId = "user1";

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setUserId("user2");

        User user = new User();
        user.setId(userId);
        user.setUserRole(Role.ROLE_USER);

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            commentService.deleteComment(commentId, user.getId(), user.getUserRole());
        });

        assertTrue(ex.getMessage().contains("U can not delete other people's comment"));
    }
}
