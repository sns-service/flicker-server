package com.example.userserver.auth.repository;

import com.example.userserver.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Query("select token FROM RefreshToken token WHERE token.user.userId = :userId")
    Optional<RefreshToken> findByUserId(@Param("userId") int userId);

    Optional<RefreshToken> findById(Long id);

    @Query("select token From RefreshToken token WHERE token.user.userId = :userId")
    void deleteByUserId(@Param("userId") int userId);
}
