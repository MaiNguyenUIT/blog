package com.example.backend.controller;

import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.CommentDTO;
import com.example.backend.dto.response.BlogResponse;
import com.example.backend.dto.response.CommentResponse;
import com.example.backend.model.Blog;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.service.BlogService;
import com.example.backend.service.CommentService;
import com.example.backend.service.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ApiResponse<BlogDTO>> createBlog(@RequestBody BlogDTO blogDTO){
        User user = userService.findUserFromToken();
        BlogDTO blog = blogService.createBlog(user.getId(), blogDTO);
        ApiResponse<BlogDTO> apiResponse = ApiResponse.<BlogDTO>builder()
                .data(blog)
                .message("Create blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<ApiResponse<BlogDTO>> updateBlog(@RequestBody BlogDTO blogDTO,
                                                           @PathVariable String blogId){
        User user = userService.findUserFromToken();
        BlogDTO blog = blogService.updateBlog(user.getId(), blogId, blogDTO);
        ApiResponse<BlogDTO> apiResponse = ApiResponse.<BlogDTO>builder()
                .data(blog)
                .message("Update blog successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlog(@PathVariable String blogId){
        Blog blog = blogService.getBlog(blogId);
        return ResponseEntity.ok(blog);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<BlogResponse>> getAllBlog(){
        List<BlogResponse> blogs = blogService.getALlBlog();
        return ResponseEntity.ok(blogs);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/public")
    public ResponseEntity<List<BlogResponse>> getAllPublicBlog(){
        List<BlogResponse> blogs = blogService.getAllPublicBlog();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Blog>> getBlogByUserId(@PathVariable String userId){
        List<Blog> blogs = blogService.getUserBlog(userId);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Blog>> getOwnBlog(){
        User user = userService.findUserFromToken();
        List<Blog> blogs = blogService.getUserBlog(user.getId());
        return ResponseEntity.ok(blogs);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Map<String, String>> deleteBlogById(@PathVariable String blogId){
        blogService.deleteBlog(blogId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Delete blog successfully"));
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Blog>> sortBlogByPriority(){
        List<Blog> blogs = blogService.getBlogsSortedByPriority();
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/{blogId}/comments")
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(@PathVariable String blogId, @RequestBody CommentDTO commentDTO){
        User user = userService.findUserFromToken();
        CommentDTO comment = commentService.createComment(user.getId(), blogId, commentDTO);
        ApiResponse<CommentDTO> apiResponse = ApiResponse.<CommentDTO>builder()
                .data(comment)
                .message("Create comment successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{blogId}/comments")
    public ResponseEntity<List<CommentResponse>> getBlogComment(@PathVariable String blogId){
        List<CommentResponse> comments = commentService.getBlogComment(blogId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/public")
    public ResponseEntity<Void> publicBlogs(@RequestParam boolean isPublic, @RequestBody List<String> blogIds){
        blogService.publicBlogs(blogIds, isPublic);
        System.out.println("public");
        return ResponseEntity.ok().build();
    }
}
