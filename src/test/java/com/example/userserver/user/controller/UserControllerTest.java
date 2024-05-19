package com.example.userserver.user.controller;

import com.example.userserver.user.dto.UserInfo;
import com.example.userserver.user.dto.UserRequest;
import com.example.userserver.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testSignUpUser() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testUser");
        userRequest.setEmail("test@example.com");
        userRequest.setPlainPassword("password");

        UserInfo userInfo = new UserInfo(1, "testUser", "test@example.com");
        when(userService.createUser(any(UserRequest.class))).thenReturn(userInfo);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\", \"email\":\"test@example.com\", \"plainPassword\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void testGetUserInfo() throws Exception {
        UserInfo userInfo = new UserInfo(1, "testUser", "test@example.com");
        when(userService.getUserById(1)).thenReturn(userInfo);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void testGetUserInfoByName() throws Exception {
        UserInfo userInfo = new UserInfo(1, "testUser", "test@example.com");
        when(userService.getUserByName("testUser")).thenReturn(userInfo);

        mockMvc.perform(get("/api/users/name/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void testSignIn() throws Exception {
        UserRequest signInRequest = new UserRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPlainPassword("password");

        UserInfo userInfo = new UserInfo(1, "testUser", "test@example.com");
        when(userService.signIn(any(UserRequest.class))).thenReturn(userInfo);

        mockMvc.perform(post("/api/users/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\", \"plainPassword\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }
}