package com.example.backend;

import com.example.backend.dto.BlogDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.BlogMapper;
import com.example.backend.model.Blog;
import com.example.backend.repository.BlogRepository;
import com.example.backend.serviceImpl.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BlogServiceTest {
    @InjectMocks
    private BlogService blogService;
    @Mock
    private BlogRepository blogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBlog(){
        String userId = "testUserId";
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setContent("testContent");
        blogDTO.setTitle("testTitle");

        Blog blog = BlogMapper.INSTANCE.toEntity(blogDTO);
        blog.setUserId(userId);

        Mockito.when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(blog);

        BlogDTO result = blogService.createBlog(userId, blogDTO);

        assertNotNull(result);
        assertEquals(blogDTO.getTitle(), result.getTitle());
        assertEquals(blogDTO.getContent(), result.getContent());

        Mockito.verify(blogRepository, Mockito.times(1)).save(Mockito.any(Blog.class));
    }

    @Test
    void testUpdateBlog(){
        String userId = "testUserId";
        String blogId = "testBlogId";

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setContent("updateContent");
        blogDTO.setTitle("updateTitle");

        Blog blog = new Blog();
        blog.setUserId(userId);
        blog.setTitle("testTitle");
        blog.setContent("testContent");
        blog.setId(blogId);


        Mockito.when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(blog);
        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        BlogDTO result = blogService.updateBlog(userId, blogId, blogDTO);

        assertNotNull(result);
        assertEquals(blogDTO.getTitle(), result.getTitle());
        assertEquals(blogDTO.getContent(), result.getContent());


        Mockito.verify(blogRepository, Mockito.times(1)).save(Mockito.any(Blog.class));
    }

    @Test
    void testGetBlog(){
        String userId = "testUserId";
        String blogId = "testBlogId";

        Blog blog = new Blog();
        blog.setUserId(userId);
        blog.setTitle("testTitle");
        blog.setContent("testContent");
        blog.setId(blogId);
        blog.setPriority(1);

        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        Blog result = blogService.getBlog(blogId);

        assertNotNull(result);
        assertEquals(blog.getTitle(), result.getTitle());
        assertEquals(blog.getContent(), result.getContent());
        assertEquals(blog.getUserId(), result.getUserId());
        assertEquals(blog.getPriority(), result.getPriority());
    }

    @Test
    void testDeleteBlog(){
        String userId = "testUserId";
        String blogId = "testBlogId";

        Blog blog = new Blog();
        blog.setUserId(userId);
        blog.setTitle("testTitle");
        blog.setContent("testContent");
        blog.setId(blogId);

        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        Mockito.doNothing().when(blogRepository).deleteById(blogId);

        blogService.deleteBlog(blogId);
        Mockito.verify(blogRepository, Mockito.times(1)).deleteById(blogId);
    }

    @Test
    void testGetUserBlog(){
        String userId = "testUserId";
        String blogId = "testBlogId";

        Blog blog = new Blog();
        blog.setUserId(userId);
        blog.setTitle("testTitle");
        blog.setContent("testContent");
        blog.setId(blogId);

        List<Blog> blogs = new ArrayList<>();
        blogs.add(blog);

        Mockito.when(blogRepository.findByuserId(userId)).thenReturn(blogs);

        List<Blog> result = blogService.getUserBlog(userId);

        assertEquals(blogs.get(0).getContent(), result.get(0).getContent());
        assertEquals(blogs.get(0).getUserId(), result.get(0).getUserId());
        assertEquals(blogs.get(0).getTitle(), result.get(0).getTitle());
        Mockito.verify(blogRepository, Mockito.times(1)).findByuserId(userId);
    }

    @Test
    void testGetAllBlog(){
        String userId1 = "testUserId1";
        String blogId1 = "testBlogId1";
        String userId2 = "testUserId2";
        String blogId2 = "testBlogId2";

        Blog blog1 = new Blog();
        blog1.setUserId(userId1);
        blog1.setTitle("testTitle1");
        blog1.setContent("testContent1");
        blog1.setId(blogId1);

        Blog blog2 = new Blog();
        blog2.setUserId(userId2);
        blog2.setTitle("testTitle2");
        blog2.setContent("testContent2");
        blog2.setId(blogId2);

        List<Blog> blogs = new ArrayList<>();
        blogs.add(blog1);
        blogs.add(blog2);

        Mockito.when(blogRepository.findAll()).thenReturn(blogs);

        List<Blog> result = blogService.getALlBlog();

        assertEquals(blogs.get(0).getContent(), result.get(0).getContent());
        assertEquals(blogs.get(0).getUserId(), result.get(0).getUserId());
        assertEquals(blogs.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(blogs.size(), result.size());
        Mockito.verify(blogRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testGetSortBlog(){
        String userId1 = "testUserId1";
        String blogId1 = "testBlogId1";
        String userId2 = "testUserId2";
        String blogId2 = "testBlogId2";

        Blog blog1 = new Blog();
        blog1.setUserId(userId1);
        blog1.setTitle("testTitle1");
        blog1.setContent("testContent1");
        blog1.setId(blogId1);
        blog1.setCreatedDate(LocalDateTime.of(2024, 1, 1, 10, 30));

        Blog blog2 = new Blog();
        blog2.setUserId(userId2);
        blog2.setTitle("testTitle2");
        blog2.setContent("testContent2");
        blog2.setId(blogId2);
        blog2.setCreatedDate(LocalDateTime.of(2025, 1, 1, 10, 30));

        List<Blog> blogs = new ArrayList<>();
        blogs.add(blog2);
        blogs.add(blog1);

        Mockito.when(blogRepository.findAll(Sort.by(Sort.Order.desc("createdDate")))).thenReturn(blogs);

        List<Blog> result = blogService.getBlogsSortedByPriority();

        assertEquals(blogs.get(0), blog2);
        assertEquals(blogs.size(), result.size());
        Mockito.verify(blogRepository, Mockito.times(1)).findAll(Sort.by(Sort.Order.desc("createdDate")));
    }

    @Test
    void testUpdateBlog_notFound(){
        String blogId = "nonExistBlog";
        String userId = "userId";

        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.empty());
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setContent("updateContent");
        blogDTO.setTitle("updateTitle");

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            blogService.updateBlog(userId, blogId, new BlogDTO());
        });

        assertTrue(ex.getMessage().contains("Blog is not found with id: " + blogId));
    }

    @Test
    void testGetBlog_notFound(){
        String blogId = "nonExistBlog";
        String userId = "userId";

        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.empty());


        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            blogService.getBlog(blogId);
        });

        assertTrue(ex.getMessage().contains("Blog is not found with id: " + blogId));
    }

    @Test
    void testUpdateBlog_cannotUpdate(){
        String blogId = "blogId";
        String userId = "userId";

        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setUserId(userId);

        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setContent("updateContent");
        blogDTO.setTitle("updateTitle");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            blogService.updateBlog("otherId", blogId, new BlogDTO());
        });

        assertTrue(ex.getMessage().contains("You can't update other people's blog"));
    }
}
