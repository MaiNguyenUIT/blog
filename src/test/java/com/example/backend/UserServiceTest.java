package com.example.backend;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.backend.ENUM.Role;
import com.example.backend.config.JwtProvider;
import com.example.backend.dto.request.UpdateInforRequest;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Blog;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.serviceImpl.CloudinaryService;
import com.example.backend.serviceImpl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;
    @Mock
    private Cloudinary cloudinary;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUser(){
        User user1 = new User();
        user1.setId("testUser1");
        user1.setUsername("test1");

        User user2 = new User();
        user2.setId("testUser2");
        user2.setUsername("test2");

        User user3 = new User();
        user3.setId("testUser3");
        user3.setUsername("test3");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUser();

        assertNotNull(result);
        assertEquals(users.size(), result.size());
        assertEquals(users.get(0), result.get(0));
        assertEquals(users.get(users.size() - 1), result.get(result.size() - 1));
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdateUserInfor(){
        User user = new User();
        user.setId("testUser1");
        user.setUsername("test1");
        user.setUserRole(Role.ROLE_USER);

        UpdateInforRequest updateInforRequest = new UpdateInforRequest();
        updateInforRequest.setName("updatedTest");
        updateInforRequest.setUserRole(Role.ROLE_ADMIN);
        updateInforRequest.setPassword("testPassword");

        Mockito.when(userRepository.findById("testUser1")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(updateInforRequest.getPassword())).thenReturn("test");

        User result = userService.updateUserInfor("testUser1", updateInforRequest);

        assertNotNull(result);
        assertEquals(updateInforRequest.getUserRole(), result.getUserRole());
        assertEquals(updateInforRequest.getName(), result.getUsername());
    }

    @Test
    void testDeleteUser(){
        User user = new User();
        user.setId("testUser1");
        user.setUsername("test1");
        user.setUserRole(Role.ROLE_USER);

        Mockito.doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteUser(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }

    @Test
    void testUploadImage() throws IOException {
        User user = new User();
        user.setId("testUser1");
        user.setUsername("test1");
        user.setUserRole(Role.ROLE_USER);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        String uploadedUrl = "https://res.cloudinary.com/avatar.jpg";
        Mockito.when(cloudinaryService.uploadFile(mockFile)).thenReturn(uploadedUrl);

        User savedUser = new User();
        savedUser.setId("testUser1");
        savedUser.setUsername("test1");
        savedUser.setAvatarUrl(uploadedUrl);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        User result = userService.uploadImage(user, mockFile);

        assertNotNull(result);
        assertEquals(uploadedUrl, result.getAvatarUrl());

        Mockito.verify(cloudinaryService, Mockito.times(1)).uploadFile(mockFile);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testFindUserFromToken() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);


        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);


        Mockito.when(userRepository.findByemail(email)).thenReturn(user);

        User result = userService.findUserFromToken();

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testFindUserFromToken_UserNotFound() {
        String email = "nonExist@example.com";

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);


        Mockito.when(userRepository.findByemail(email)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            userService.findUserFromToken();
        });

        assertTrue(ex.getMessage().contains("User is not found with email: " + email));
    }
}
