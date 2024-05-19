package com.example.userserver.user.service;

import com.example.userserver.exception.BadRequestException;
import com.example.userserver.exception.DataNotFoundException;
import com.example.userserver.user.dto.UserInfo;
import com.example.userserver.user.dto.UserRequest;
import com.example.userserver.user.entity.User;
import com.example.userserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    public UserInfo createUser(UserRequest userRequest) {
        String hashedPassword = encoder.encode(userRequest.getPlainPassword());

        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            throw new BadRequestException("Username Duplicated.");
        }

        User user = new User(userRequest.getUsername(), userRequest.getEmail(), hashedPassword);

        User savedUser = userRepository.save(user);

        return new UserInfo(savedUser);
    }

    public UserInfo getUserById(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new DataNotFoundException("user not found");
        }

        return new UserInfo(user);
    }

    public UserInfo getUserByName(String name) {
        User user = userRepository.findByUsername(name);
        if (user == null) {
           throw new DataNotFoundException("user not found");
        }

        return new UserInfo(user);
    }

    public UserInfo signIn(UserRequest signInRequest) {
        User user = userRepository.findByUsername(signInRequest.getUsername());

        if (user == null) {
            throw new RuntimeException("no username found!");
        }

        boolean isPasswordMatch = encoder.matches(signInRequest.getPlainPassword(), user.getPassword());

        if (isPasswordMatch) {
            return new UserInfo(user);
        } else {
            throw new BadRequestException();
        }
    }
}
