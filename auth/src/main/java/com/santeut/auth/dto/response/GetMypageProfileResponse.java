package com.santeut.auth.dto.response;

import com.santeut.auth.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetMypageProfileResponse {

    private int userId;
    private String userLoginId;
    private String userNickname;
    private String userTierName;
    private int userTierPoint;
    private int userDistance;
    private int userMoveTime;
    private int userHikingCount;
    private int userHikingMountain;

    public GetMypageProfileResponse(UserEntity userEntity, int point, String userTierName){
        this.userId = userEntity.getUserId();
        this.userLoginId = userEntity.getUserLoginId();
        this.userNickname = userEntity.getUserNickname();
        this.userTierName = userTierName;
        this.userTierPoint = point;
        this.userDistance = userEntity.getUserDistance();
        this.userMoveTime = userEntity.getUserMoveTime();
        this.userHikingCount = userEntity.getUserHikingCount();
        this.userHikingMountain = userEntity.getUserHikingMountain();
    }
}
