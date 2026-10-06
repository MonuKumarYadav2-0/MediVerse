package com.mediverse.mapper;

import org.springframework.stereotype.Component;

import com.mediverse.dto.UserResponse;
import com.mediverse.entity.User;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}