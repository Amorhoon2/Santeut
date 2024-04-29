package com.santeut.auth.domain.entity;

import com.santeut.auth.domain.dto.requestDto.SignUpRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotNull
    @Column(length = 15)
    private String userLoginId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String userPassword;

    @NotNull
    @Column(length = 15)
    private String userNickname;

    @NotNull
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime modifiedAt;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    @Column(length = 6)
    private String userBirth;

    @NotNull
    private String userGender;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime deletedAt;

    @Column(columnDefinition = "TEXT")
    private String userProfile;

    public static UserEntity signUp(SignUpRequestDto dto){

        UserEntity userEntity = new UserEntity();
        userEntity.userNickname = dto.getUserNickname();
        userEntity.userLoginId = dto.getUserLoginId();
        userEntity.userPassword = dto.getUserPassword();
        userEntity.userBirth = dto.getUserBirth();
        userEntity.userProfile = dto.getUserProfile();
        userEntity.userGender = dto.getUserGender();
        userEntity.createdAt = LocalDateTime.now();
        userEntity.modifiedAt = LocalDateTime.now();
        userEntity.isDeleted = false;

        return userEntity;
    }


    public UserEntity getUserInfo(UserEntity userEntity){

        UserEntity user = new UserEntity();

        user.userId = userEntity.getUserId();
        user.userNickname = userEntity.getUserNickname();
        user.userLoginId = userEntity.getUserLoginId();
        user.userBirth = userEntity.getUserBirth();
        user.userProfile = userEntity.getUserBirth();
        user.userGender = userEntity.getUserGender();
        user.createdAt = userEntity.createdAt;
        user.modifiedAt = userEntity.modifiedAt;

        return user;
    }
}
