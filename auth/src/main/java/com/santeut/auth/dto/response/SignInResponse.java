package com.santeut.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {

    private String userNickname;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    public SignInResponse(JwtTokenResponseDto jwtTokenResponseDto, String userNickname){
        this.userNickname = userNickname;
        this.grantType = jwtTokenResponseDto.getGrantType();
        this.accessToken = jwtTokenResponseDto.getAccessToken();
        this.refreshToken = jwtTokenResponseDto.getRefreshToken();
    }
}
