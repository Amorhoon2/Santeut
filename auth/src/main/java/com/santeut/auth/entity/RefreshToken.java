package com.santeut.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 31536000)
public class RefreshToken {
    @Id
    private String userId;
    private String refreshToken;

}