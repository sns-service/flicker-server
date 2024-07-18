package com.example.server.follow.controller;

import com.example.server.follow.dto.FollowInfo;
import com.example.server.follow.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FollowControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FollowService followService;

    @InjectMocks
    private FollowController followController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(followController).build();
    }

    @Test
    @DisplayName("팔로워 리스트 조회 API")
    void listFollowers() throws Exception {
        when(followService.listFollower(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/follows/followers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("팔로우 리스트 조회 API")
    void listFollowings() throws Exception {
        when(followService.listFollowing(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/follows/followings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("팔로우 여부 조회 API")
    void isFollow() throws Exception {
        when(followService.isFollow(1, 2)).thenReturn(true);

        mockMvc.perform(get("/api/follows/follow/1/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("팔로우 요청 API 테스트")
    void followUser() throws Exception {
        FollowInfo followInfo = FollowInfo.builder().followId(1).userId(1).followerId(2).build();
        when(followService.followUser(1, 2)).thenReturn(followInfo);

        mockMvc.perform(post("/api/follows/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"followerId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.followerId").value(2));
    }

    @Test
    @DisplayName("언팔로우 테스트")
    void unfollowUser() throws Exception {
        when(followService.unfollowUser(1, 2)).thenReturn(true);

        mockMvc.perform(post("/api/follows/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"followerId\":2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}