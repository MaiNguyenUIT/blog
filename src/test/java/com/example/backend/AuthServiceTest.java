package com.example.backend;

import com.example.backend.config.JwtProvider;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.UserAccountMapper;
import com.example.backend.model.Blog;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.serviceImpl.AuthService;
import com.example.backend.serviceImpl.CustomerUserDetailService;
import com.example.backend.utils.ValidationAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerUserDetailService customerUserDetailService;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private ValidationAccount validationAccount;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignIn() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        String email = "testEmail@gmail.com";
        String password = "testPassword";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        String jwtToken = "mocked.jwt.token";

        UserDetails userDetails = new org.springframework.security.core.userdetails.User("testEmail@gmail.com",
                encodedPassword,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());

        when(userRepository.findByemail("testEmail@gmail.com")).thenReturn(user);
        when(customerUserDetailService.loadUserByUsername(email)).thenReturn(userDetails);
        when(passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())).thenReturn(true);
        when(jwtProvider.generatedToken(authentication)).thenReturn(jwtToken);


        AuthResponse response = authService.signIn(loginRequest);

        assertNotNull(response);
        assertEquals(jwtToken, response.getJwt());

    }

    @Test
    void TestSignIn_InvalidEmail() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        String email = "nonExist@gmail.com";
        String password = "testPassword";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);



        when(passwordEncoder.encode("testPassword")).thenReturn(encodedPassword);
        when(userRepository.findByemail("nonExist@gmail.com")).thenReturn(null);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("testEmail@gmail.com",
                encodedPassword,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            authService.signIn(loginRequest);
        });

        assertTrue(ex.getMessage().contains("Invalid username...."));
    }

    @Test
    void TestSignIn_InvalidPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        String email = "test@gmail.com";
        String password = "testPassword";
        String encodedPassword = new BCryptPasswordEncoder().encode("hihihaha");

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User("testEmail@gmail.com",
                encodedPassword,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())).thenReturn(false);
        when(userRepository.findByemail("test@gmail.com")).thenReturn(user);
        when(customerUserDetailService.loadUserByUsername(email)).thenReturn(userDetails);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            authService.signIn(loginRequest);
        });

        assertTrue(ex.getMessage().contains("Invalid password"));
    }

    @Test
    void testSignUp() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("Test@1234");
        request.setUsername("test");

        User user = UserAccountMapper.INSTANCE.toEntity(request);

        Mockito.when(userRepository.findByemail("test@gmail.com")).thenReturn(null);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);


        authService.signUp(request);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testSignUp_ValidEmail() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("Test@1234");
        request.setUsername("test");

        User user = UserAccountMapper.INSTANCE.toEntity(request);

        Mockito.when(userRepository.findByemail("test@gmail.com")).thenReturn(user);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            authService.signUp(request);
        });

        assertTrue(ex.getMessage().contains("User is already existed with email " + request.getEmail()));
    }

    @Test
    void testSignUp_WrongEmail() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test.gmail.com");
        request.setPassword("Test@1234");
        request.setUsername("test");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            authService.signUp(request);
        });

        assertTrue(ex.getMessage().contains("Email or password is incorrect"));
    }

    @Test
    void testSignUp_WrongPassword() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("1234");
        request.setUsername("test");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            authService.signUp(request);
        });

        assertTrue(ex.getMessage().contains("Email or password is incorrect"));
    }
}
