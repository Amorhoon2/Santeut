package com.santeut.auth.dto.response;

import com.santeut.auth.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoResponse {

    private int userId;
    private String userLoginId;
    private String userNickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String userBirth;
    private char userGender;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;
    private String userProfile;

    public GetUserInfoResponse(UserEntity userEntity){
        this.userId = userEntity.getUserId();
        this.userLoginId = userEntity.getUserLoginId();
        this.userNickname = userEntity.getUserNickname();
        this.createdAt = userEntity.getCreatedAt();
        this.modifiedAt = userEntity.getModifiedAt();
        this.userBirth = userEntity.getUserBirth();
        this.userGender = userEntity.getUserGender();
        this.deletedAt = userEntity.getDeletedAt();
        this.isDeleted = userEntity.getIsDeleted();
        this.userProfile = userEntity.getUserProfile();
    }

}
