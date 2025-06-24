package com.popcornpicks.controllers;

import com.popcornpicks.dto.UserRegistrationRequest;
import com.popcornpicks.dto.UserResponse;
import com.popcornpicks.exceptions.EmailAlreadyExistsException;
import com.popcornpicks.mapper.UserMapper;
import com.popcornpicks.models.User;
import com.popcornpicks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRegistrationRequest registrationRequest) {

        try {
            User toCreate = userMapper.toEntity(registrationRequest);
            User created = userService.register(toCreate);
            UserResponse response = userMapper.toDto(created);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (EmailAlreadyExistsException ex) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, ex.getMessage(), ex
            );
        }
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            UserResponse response = userMapper.toDto(userOpt.get());
            return ResponseEntity.ok(response);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found with id " + id
            );
        }
    }
}
