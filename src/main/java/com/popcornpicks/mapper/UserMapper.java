package com.popcornpicks.mapper;

import com.popcornpicks.dto.UserRegistrationRequest;
import com.popcornpicks.dto.UserResponse;
import com.popcornpicks.models.User;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserMapper {


    public User toEntity(UserRegistrationRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRoles(Collections.singleton("USER"));
        return user;
    }


    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
