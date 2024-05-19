package com.example.userserver.user.service;

import com.example.userserver.exception.BadRequestException;
import com.example.userserver.exception.DataNotFoundException;
import com.example.userserver.user.dto.UserInfo;
import com.example.userserver.user.dto.UserRequest;
import com.example.userserver.user.entity.User;
import com.example.userserver.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유저 생성 테스트")
    void createUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testUser");
        userRequest.setEmail("test@example.com");
        userRequest.setPlainPassword("password");

        User user = new User("testUser", "test@example.com", "hashedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserInfo result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("유저 생성 테스트 - Username 중복")
    void createUser_name_duplicated() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testUser");
        userRequest.setEmail("test@email.com");
        userRequest.setPlainPassword("pw123");

        when(userRepository.findByUsername("testUser")).thenReturn(new User());

        assertThrows(BadRequestException.class, () -> userService.createUser(userRequest));
    }

    @Test
    @DisplayName("userId로 유저 조회 테스트")
    void getUserById() {
        User user = new User("testUser", "test@example.com", "password");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserInfo result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    @DisplayName("존재하지 않는 유저 id로 조회할 때")
    void getUserById_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    @DisplayName("이름으로 유저 조회")
    void getUserByName() {
        User user = new User("testUser", "test@example.com", "password");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserInfo result = userService.getUserByName("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    @DisplayName("username 으로 찾았는데, 없는 경우")
    void getUserByName_NotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        assertThrows(DataNotFoundException.class, () -> userService.getUserByName("testUser"));
    }

    @Test
    @DisplayName("로그인 테스트")
    void signIn() {
        User user = new User("testUser", "test@example.com", new BCryptPasswordEncoder().encode("password"));
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);

        UserRequest signInRequest = new UserRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPlainPassword("password");

        UserInfo result = userService.signIn(signInRequest);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀렸을 때")
    void signIn_WrongPassword() {
        User user = new User("testUser", "test@example.com", new BCryptPasswordEncoder().encode("password"));
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserRequest signInRequest = new UserRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPlainPassword("wrongPassword");

        assertThrows(BadRequestException.class, () -> userService.signIn(signInRequest));
    }
}