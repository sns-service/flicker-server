package com.example.server.follow.repository;

import com.example.server.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowJpaRepository extends JpaRepository<Follow, Integer> {

}
