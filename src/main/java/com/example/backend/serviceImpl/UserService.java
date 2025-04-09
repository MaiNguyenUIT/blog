package com.example.backend.serviceImpl;

import com.example.backend.config.JwtProvider;
import com.example.backend.dto.request.UpdateInforRequest;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserService implements com.example.backend.service.UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserInfor(String userId, UpdateInforRequest updateInforRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found with id: " + userId));
        user.setUserRole(updateInforRequest.getUserRole());
        user.setUsername(updateInforRequest.getName());
        user.setPassword(passwordEncoder.encode(updateInforRequest.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }


    @Override
    public User uploadImage(User user, MultipartFile file) throws IOException {
        String avatarUrl = cloudinaryService.uploadFile(file);
        user.setAvatarUrl(avatarUrl);

        return userRepository.save(user);
    }

    @Override
    public User findUserFromToken() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByemail(email);
        if(user == null){
            System.out.println("User is not found ");
            throw new NotFoundException("User is not found with email: " + email);
        }
        return user;
    }

}
