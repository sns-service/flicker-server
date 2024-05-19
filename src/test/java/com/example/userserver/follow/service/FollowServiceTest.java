package com.example.userserver.follow.service;

import com.example.userserver.exception.BadRequestException;
import com.example.userserver.follow.dto.FollowInfo;
import com.example.userserver.follow.entity.Follow;
import com.example.userserver.follow.repository.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowService followService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("팔로우 여부 확인 테스트")
    void isFollow_Test() {
        when(followRepository.findByUserIdAndFollowerId(1, 2)).thenReturn(Optional.of(new Follow(1, 2)));

        assertTrue(followService.isFollow(1, 2));
        verify(followRepository, times(1)).findByUserIdAndFollowerId(1, 2);
    }

    @Test
    @DisplayName("이미 팔로우 한 경우에는, null을 반환한다.")
    void followUser_Test() {
        Follow follow = new Follow(1, 2);

        // follow 엔티티가 아직 존재하지 않는다.
        when(followRepository.findByUserIdAndFollowerId(1, 2)).thenReturn(Optional.empty());
        when(followRepository.save(any(Follow.class))).thenReturn(follow);

        FollowInfo followInfo = followService.followUser(1, 2);

        assertNotNull(followInfo);
        assertEquals(1, followInfo.getUserId());
        assertEquals(2, followInfo.getFollowerId());

        verify(followRepository, times(1)).findByUserIdAndFollowerId(1, 2);
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    @DisplayName("이미 팔로우 한 경우, null 반환")
    void alreadyFollowed() {
        when(followRepository.findByUserIdAndFollowerId(1, 2)).thenReturn(Optional.of(new Follow(1, 2)));
        FollowInfo followInfo = followService.followUser(1, 2);

        assertNull(followInfo);
        verify(followRepository, times(1)).findByUserIdAndFollowerId(1, 2);
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    @DisplayName("언팔로우 시, true 결과깂 반환")
    void unfollow() {
        Follow follow = new Follow(1, 2);
        when(followRepository.findByUserIdAndFollowerId(1, 2)).thenReturn(Optional.of(follow));

        boolean result = followService.unfollowUser(1, 2);

        assertTrue(result);
        verify(followRepository, times(1)).findByUserIdAndFollowerId(1, 2);
        verify(followRepository, times(1)).delete(follow);
    }

    @Test
    @DisplayName("없는 Follow 엔터티 요청 시, 에러 반환")
    void unfollow_NotFound() {
        when(followRepository.findByUserIdAndFollowerId(1, 2)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> followService.unfollowUser(1, 2));
        verify(followRepository, times(1)).findByUserIdAndFollowerId(1, 2);
        verify(followRepository, never()).delete(any(Follow.class));
    }

    @Test
    @DisplayName("유저 팔로워 리스트 조회")
    void listFollower() {
        when(followRepository.findFollowersByUserId(1)).thenReturn(Collections.emptyList());

        assertTrue(followService.listFollower(1).isEmpty());
        verify(followRepository, times(1)).findFollowersByUserId(1);
    }

    @Test
    @DisplayName("유저 팔로우 리스트 조회")
    void listFollowing() {
        when(followRepository.findFollowingByUserId(1)).thenReturn(Collections.emptyList());

        assertTrue(followService.listFollowing(1).isEmpty());
        verify(followRepository, times(1)).findFollowingByUserId(1);
    }
}