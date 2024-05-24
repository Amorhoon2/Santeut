package com.santeut.auth.repository;

import com.santeut.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByUserNickname(String userNickname);
    boolean existsByUserLoginId(String userLoginId);
    Optional<UserEntity> findByUserLoginId(String userLoginId);
    Optional<UserEntity> findByUserId(int userId);
}
