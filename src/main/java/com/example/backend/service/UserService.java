package com.example.backend.service;

import com.example.backend.dto.request.UpdateInforRequest;
import com.example.backend.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<User> getAllUser();
    User updateUserInfor(String userId, UpdateInforRequest updateInforRequest);
    void deleteUser(String userId);
    User uploadImage(User user, MultipartFile file) throws IOException;
    User findUserFromToken();
}
