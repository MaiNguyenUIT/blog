package com.example.backend;

import com.example.backend.ENUM.Role;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.serviceImpl.CustomerUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerUserDetailServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerUserDetailService customerUserDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername(){
        User user1 = new User();
        String email = "test@gmail.com";
        user1.setId("testUser1");
        user1.setUsername("test1");
        user1.setPassword("testPassword");
        user1.setEmail(email);
        user1.setUserRole(Role.ROLE_USER);


        Mockito.when(userRepository.findByusername(user1.getUsername())).thenReturn(user1);

        UserDetails result = customerUserDetailService.loadUserByUsername(user1.getUsername());

        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertEquals(user1.getEmail(), result.getUsername());
    }

    @Test
    void testLoadUserByUsername_EmailNotFound(){
        User user1 = new User();
        String email = "nonExist@gmail.com";
        user1.setId("testUser1");
        user1.setUsername("test1");
        user1.setPassword("testPassword");
        user1.setEmail(email);
        user1.setUserRole(Role.ROLE_USER);


        Mockito.when(userRepository.findByusername(user1.getUsername())).thenReturn(null);

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, () -> {
            customerUserDetailService.loadUserByUsername(user1.getUsername());
        });

        assertTrue(ex.getMessage().contains("user not found with username " + user1.getUsername()));
    }
}
