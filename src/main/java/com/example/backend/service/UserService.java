package com.example.backend.service;

import com.example.backend.dto.request.UpdateInforRequest;
import com.example.backend.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    User updateUserInfor(String userId, UpdateInforRequest updateInforRequest);
    void deleteUser(String userId);
    User findUserByJwtToken(String jwt);
}
