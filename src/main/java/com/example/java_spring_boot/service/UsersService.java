package com.example.java_spring_boot.service;

import com.example.java_spring_boot.dao.entity.UsersEntity;
import com.example.java_spring_boot.dto.request.UserRequest;
import com.example.java_spring_boot.dto.response.UserResponse;
import com.example.java_spring_boot.enums.UserAuthorityEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {
    void createUser(UserRequest request);
    List<UserResponse> getAllUsers(List<UserAuthorityEnum> authorities);
    UserResponse getUser(String id);
    void updateUser(String id, UserRequest request);
    String deleteUser(String id);
    UsersEntity getUserByEmail(String email);
}
