package com.example.backend.serviceImpl;

import com.example.backend.config.JwtProvider;
import com.example.backend.dto.request.UpdateInforRequest;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService implements com.example.backend.service.UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        user.setUserRole(updateInforRequest.getUserRole());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserByJwtToken(String jwt) {
        String email = jwtProvider.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByemail(email);
        if(user == null){
            System.out.println("User is not found with email in jwt");
            throw new NotFoundException("User is not found with email: " + email);
        }
        return user;
    }
}
