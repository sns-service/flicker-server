package com.example.server.follow.repository;

import com.example.server.user.dto.UserInfo;
import com.example.server.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

    Optional<Follow> findByUserIdAndFollowerId(int userId, int followerId);

    // 팔로워 목록 조회
    @Query(value = "SELECT new com.example.server.user.dto.UserInfo(u.userId, u.username, u.email) FROM Follow f, User u WHERE f.userId = :userId and f.followerId = u.userId")
    List<UserInfo> findFollowersByUserId(@Param("userId") int userId);

    // 팔로윙 목록 조회
    @Query(value = "SELECT new com.example.server.user.dto.UserInfo(u.userId, u.username, u.email) FROM Follow f, User u WHERE f.followerId = :userId and f.userId = u.userId")
    List<UserInfo> findFollowingByUserId(@Param("userId") int userId);
}
