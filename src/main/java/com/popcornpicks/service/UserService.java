package com.popcornpicks.service;

import com.popcornpicks.models.User;
import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}


