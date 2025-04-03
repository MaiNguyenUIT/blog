package com.example.backend.controller;

import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.CommentDTO;
import com.example.backend.model.Blog;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.service.BlogService;
import com.example.backend.service.CommentService;
import com.example.backend.service.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/blogs")
@RestController
public class BlogController {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    @PostMapping()
    public ResponseEntity<ApiResponse<BlogDTO>> createBlog(@RequestHeader("Authorization") String jwt, @RequestBody BlogDTO blogDTO){
        User user = userService.findUserByJwtToken(jwt);
        BlogDTO blog = blogService.createBlog(user.getId(), blogDTO);
        ApiResponse<BlogDTO> apiResponse = ApiResponse.<BlogDTO>builder()
                .status(200)
                .data(blog)
                .message("Create blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<ApiResponse<BlogDTO>> updateBlog(@RequestHeader("Authorization") String jwt,
                                                           @RequestBody BlogDTO blogDTO,
                                                           @PathVariable String blogId){
        User user = userService.findUserByJwtToken(jwt);
        BlogDTO blog = blogService.updateBlog(user.getId(), blogId, blogDTO);
        ApiResponse<BlogDTO> apiResponse = ApiResponse.<BlogDTO>builder()
                .status(200)
                .data(blog)
                .message("Update blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<ApiResponse<Blog>> getBlog(@RequestHeader("Authorization") String jwt,
                                                           @PathVariable String blogId){
        Blog blog = blogService.getBlog(blogId);
        ApiResponse<Blog> apiResponse = ApiResponse.<Blog>builder()
                .status(200)
                .data(blog)
                .message("Get blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Blog>>> getAllBlog(@RequestHeader("Authorization") String jwt){
        List<Blog> blogs = blogService.getALlBlog();
        ApiResponse<List<Blog>> apiResponse = ApiResponse.<List<Blog>>builder()
                .status(200)
                .data(blogs)
                .message("Get all blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Blog>>> getBlogByUserId(@RequestHeader("Authorization") String jwt,
                                                     @PathVariable String userId){
        User user = userService.findUserByJwtToken(jwt);
        List<Blog> blogs = blogService.getUserBlog(userId);
        ApiResponse<List<Blog>> apiResponse = ApiResponse.<List<Blog>>builder()
                .status(200)
                .data(blogs)
                .message("Get user blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<ApiResponse<Void>> deleteBlogById(@RequestHeader("Authorization") String jwt,
                                                                   @PathVariable String blogId){
        blogService.deleteBlog(blogId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(200)
                .message("Delete blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/sort")
    public ResponseEntity<ApiResponse<List<Blog>>> sortBlogByPriority(@RequestHeader("Authorization") String jwt){
        List<Blog> blogs = blogService.getBlogsSortedByPriority();
        ApiResponse<List<Blog>> apiResponse = ApiResponse.<List<Blog>>builder()
                .status(200)
                .data(blogs)
                .message("Delete blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{blogId}/comments")
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(@RequestHeader("Authorization") String jwt, @PathVariable String blogId, @RequestBody CommentDTO commentDTO){
        User user = userService.findUserByJwtToken(jwt);
        CommentDTO comment = commentService.createComment(user.getId(), blogId, commentDTO);
        ApiResponse<CommentDTO> apiResponse = ApiResponse.<CommentDTO>builder()
                .status(200)
                .data(comment)
                .message("Create comment successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{blogId}/comments")
    public ResponseEntity<ApiResponse<List<Comment>>> getBlogComment(@RequestHeader("Authorization") String jwt, @PathVariable String blogId){
        List<Comment> comments = commentService.getBlogComment(blogId);
        ApiResponse<List<Comment>> apiResponse = ApiResponse.<List<Comment>>builder()
                .status(200)
                .data(comments)
                .message("Get blog comments successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
