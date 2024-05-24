package com.santeut.auth.service.implementation;

import com.santeut.auth.common.exception.DataNotFoundException;
import com.santeut.auth.dto.request.SignInRequestDto;
import com.santeut.auth.dto.request.SignUpRequestDto;
import com.santeut.auth.dto.response.JwtTokenResponseDto;
import com.santeut.auth.dto.response.SignInResponse;
import com.santeut.auth.entity.RefreshToken;
import com.santeut.auth.entity.UserEntity;
import com.santeut.auth.repository.RefreshTokenRepository;
import com.santeut.auth.repository.UserRepository;
import com.santeut.auth.service.AuthService;
import com.santeut.auth.common.jwt.JwtTokenProvider;
import com.santeut.auth.common.response.ResponseCode;
import com.santeut.auth.util.AgeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AgeUtil ageUtil;

    @Override
    @Transactional
    public void signUp(SignUpRequestDto dto) {

        String password = dto.getUserPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        boolean usedUserNickname = userRepository.existsByUserNickname(dto.getUserNickname());
        if (usedUserNickname) throw new DataNotFoundException(ResponseCode.EXISTS_USER_NICKNAME);

        boolean usedUserLoginId = userRepository.existsByUserLoginId(dto.getUserLoginId());
        if(usedUserLoginId) throw new DataNotFoundException(ResponseCode.EXISTS_USER_LOGIN_ID);

        UserEntity userEntity = UserEntity.signUp(dto);
        userEntity.setUserPassword(encodedPassword);

        int age = ageUtil.calculateAge(userEntity.getUserBirth());
        userEntity.setUserAge(age);
        userRepository.save(userEntity);
    }

    @Override
    public SignInResponse signIn(SignInRequestDto dto) {

        String userLoginId = dto.getUserLoginId();
        String userPassword = dto.getUserPassword();

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        int userId = userEntity.getUserId();
        String userNickname = userEntity.getUserNickname();

        if(!userLoginId.equals(userEntity.getUserLoginId())){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_USER_LOGIN_ID);
        }
        if (!bCryptPasswordEncoder.matches(userPassword, userEntity.getUserPassword())){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_USER_PASSWORD);
        }

        JwtTokenResponseDto jwtTokenResponse = jwtTokenProvider.issueToken(userId, userNickname);

        if(refreshTokenRepository.existsByUserId(String.valueOf(userId))){
            refreshTokenRepository.deleteByUserId(String.valueOf(userId));
        }

        refreshTokenRepository.save(new RefreshToken(String.valueOf(userId), jwtTokenResponse.getRefreshToken()));
        return new SignInResponse(jwtTokenResponse, userEntity.getUserNickname());
    }

    @Override
    public JwtTokenResponseDto reissueToken(HttpServletRequest request) {

        JwtTokenResponseDto jwtTokenResponseDto = jwtTokenProvider.reissueToken(request);

        String userLoginId = jwtTokenProvider.extractUserId(jwtTokenResponseDto.getRefreshToken());

        if(refreshTokenRepository.existsByUserId(userLoginId)){
            refreshTokenRepository.deleteByUserId(userLoginId);
        }
        refreshTokenRepository.save(new RefreshToken(userLoginId,jwtTokenResponseDto.getRefreshToken()));
        return jwtTokenResponseDto;
    }
}
