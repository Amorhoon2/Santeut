package com.santeut.auth.service.implementation;

import com.amazonaws.services.s3.AmazonS3Client;
import com.santeut.auth.common.exception.DataNotFoundException;
import com.santeut.auth.common.response.ResponseCode;
import com.santeut.auth.dto.request.HikingRecordRequest;
import com.santeut.auth.dto.request.UpdatePasswordRequest;
import com.santeut.auth.dto.request.UpdateProfileImageRequest;
import com.santeut.auth.dto.request.UpdateProfileRequest;
import com.santeut.auth.dto.response.GetMountainRecordResponse;
import com.santeut.auth.dto.response.GetMypageProfileResponse;
import com.santeut.auth.dto.response.GetUserInfoResponse;
import com.santeut.auth.dto.response.GetUserLevelResponse;
import com.santeut.auth.entity.UserEntity;
import com.santeut.auth.repository.UserRepository;
import com.santeut.auth.service.UserService;
import com.santeut.auth.util.ImageUtil;
import com.santeut.auth.util.LevelUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LevelUtil levelUtil;
    private final ImageUtil imageUtil;

    @Override
    public GetUserInfoResponse userLoginInfo(String userId) {

        UserEntity userEntity = userRepository.findByUserId(Integer.parseInt(userId))
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        return new GetUserInfoResponse(userEntity);
    }

    @Override
    public GetUserInfoResponse userInfo(int userId) {

        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));


        return new GetUserInfoResponse(userEntity);
    }

    @Override
    public void updatePassword(String userLoginId, UpdatePasswordRequest request) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        if(!bCryptPasswordEncoder.matches(request.getCurrentPassword(), userEntity.getUserPassword())){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_USER_PASSWORD);
        }

        String updatePassword = bCryptPasswordEncoder.encode(request.getUpdatePassword());
        userEntity.setUserPassword(updatePassword);
        userRepository.save(userEntity);
    }

    @Override
    public void updateProfile(String userLoginId, UpdateProfileRequest request, MultipartFile multipartFile) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        log.debug("userNickname: "+ request.getUserNickname());
        if (!request.getUserNickname().equals(userEntity.getUserNickname())) {
            boolean checkUser = userRepository.existsByUserNickname(request.getUserNickname());
            if (checkUser) throw new DataNotFoundException(ResponseCode.EXISTS_USER_NICKNAME);
            userEntity.setUserNickname(request.getUserNickname());
        }
        log.debug("currentPassword: "+ request.getCurrentPassword());
        if (request.getUpdatePassword() != null){
         if(!bCryptPasswordEncoder.matches(request.getCurrentPassword(), userEntity.getUserPassword())){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_USER_PASSWORD);
         }
         String updatePassword = bCryptPasswordEncoder.encode(request.getUpdatePassword());
         userEntity.setUserPassword(updatePassword);
        }

        if (multipartFile != null){
            String imageUrl = imageUtil.saveImage(multipartFile);
            userEntity.setUserProfile(imageUrl);
        }
        else userEntity.setUserProfile(null);

        userEntity.setModifiedAt(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    @Override
    public void updateProfileImage(String userLoginId, MultipartFile multipartFile) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        log.debug("Profile Image: "+multipartFile);

        String imageUrl = imageUtil.saveImage(multipartFile);
        userEntity.setUserProfile(imageUrl);

        userRepository.save(userEntity);
    }

    @Override
    public GetMypageProfileResponse getMypageProfile(String userLoginId) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));
        int point = userEntity.getUserPoint();
        String tierName = levelUtil.getTierName(point);
        return new GetMypageProfileResponse(userEntity, point, tierName);
    }

    @Override
    public GetUserLevelResponse getLevel(String userLoginId) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        int point = userEntity.getUserPoint();
        String tierName = levelUtil.getTierName(point);

        return new GetUserLevelResponse(tierName, point);
    }

    @Override
    public GetMountainRecordResponse getMountainRecord(String userLoginId) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        return new GetMountainRecordResponse(userEntity.getUserDistance(), userEntity.getUserMoveTime(),
                userEntity.getUserHikingCount(), userEntity.getUserHikingMountain());
    }

    @Override
    public void patchMountainRecord(HikingRecordRequest request) {

        UserEntity userEntity = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        userEntity.setUserHikingCount(userEntity.getUserHikingCount()+1);
        userEntity.setUserDistance(userEntity.getUserDistance()+request.getDistance());
        userEntity.setUserMoveTime(userEntity.getUserMoveTime()+request.getMoveTime());
        userEntity.setUserPoint(userEntity.getUserPoint()+50);
        log.debug("첫 등반 산: "+ request.getIsFirstMountain());
        if (request.getIsFirstMountain()) userEntity.setUserHikingMountain(userEntity.getUserHikingMountain()+1);

        userRepository.save(userEntity);
    }

}
