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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    @Transactional
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
            throw new BadRequestException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        return checkIfPasswordMatches(signInRequest, user);
    }

    private UserInfo checkIfPasswordMatches(UserRequest signInRequest, User user) {
        boolean isPasswordMatch = encoder.matches(signInRequest.getPlainPassword(), user.getPassword());

        if (isPasswordMatch) {
            return new UserInfo(user);
        } else {
            throw new BadRequestException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }
    }
}
