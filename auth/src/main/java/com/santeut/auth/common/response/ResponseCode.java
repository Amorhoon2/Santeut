package com.santeut.auth.common.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(HttpStatus.OK.value(), "success"),

    // 유저
    EXISTS_USER_NICKNAME(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하는 유저 닉네임"),
    EXISTS_USER_LOGIN_ID(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하는 로그인 아이디"),
    NOT_MATCH_USER_LOGIN_ID(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 아이디가 일치하지 않음"),
    NOT_MATCH_USER_PASSWORD(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "패스워드가 일치하지 않음"),
    NOT_EXISTS_USER(HttpServletResponse.SC_NOT_FOUND, "존재하지 않는 유저"),
    NOT_EXISTS_USER_LEVEL(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하지 않는 등급 정보"),
    NOT_EXISTS_UPDATE_PROFILE_INFO(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "수정할 프로필 정보가 없음"),

    // 토큰
    INVALID_ACCESS_TOKEN(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "유효하지 않은 Access Token"),
    INVALID_REFRESH_TOKEN(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "유효하지 않은 Refresh Token"),
    NOT_MATCH_REFRESH_TOKEN(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "일치하지 않는 Refresh Token");

    private final int code;
    private final String message;
}
