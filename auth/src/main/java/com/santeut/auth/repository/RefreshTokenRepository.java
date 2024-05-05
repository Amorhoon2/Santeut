package com.santeut.auth.repository;

import com.santeut.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUserId(String userId);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);

}
