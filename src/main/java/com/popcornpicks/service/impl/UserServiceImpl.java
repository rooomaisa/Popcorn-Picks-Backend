package com.popcornpicks.service.impl;

import com.popcornpicks.exceptions.EmailAlreadyExistsException;
import com.popcornpicks.models.User;
import com.popcornpicks.repository.UserRepository;
import com.popcornpicks.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Inject both UserRepository and PasswordEncoder
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already in use: " + user.getEmail()
            );
        }
        // 1) Hash the raw password before saving:
        String rawPassword = user.getPassword();
        String hashed     = passwordEncoder.encode(rawPassword);
        user.setPassword(hashed);

        // 2) Now save the user with the BCrypt‐encoded password:
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}


//package com.popcornpicks.service.impl;
//
//import com.popcornpicks.exceptions.EmailAlreadyExistsException;
//import com.popcornpicks.models.User;
//import com.popcornpicks.repository.UserRepository;
//import com.popcornpicks.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public User register(User user) {
//        if (userRepository.existsByEmail(user.getEmail())) {
//            throw new EmailAlreadyExistsException(
//                    "Email already in use: " + user.getEmail()
//            );
//        }
//
//        return userRepository.save(user);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<User> findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<User> findById(Long id) {
//        return userRepository.findById(id);
//    }
//}
