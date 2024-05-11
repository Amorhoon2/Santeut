package com.santeut.auth.controller;


import com.santeut.auth.common.response.BasicResponse;
import com.santeut.auth.dto.request.SignInRequestDto;
import com.santeut.auth.dto.request.SignUpRequestDto;
import com.santeut.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/welcome")
    public String welcome(@RequestHeader(value = "usernickname") String name,
                          @RequestHeader(value = "userNickname") String name2,
                          HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            // 예외 처리
//        request.setCharacterEncoding("UTF-8");
//        for (Map.Entry<String, String> entry : headers.entrySet()) {
//            System.out.println("key: " + entry.getKey() +
//                    ", value: " + entry.getValue());
//        }
        log.debug("name: "+ name);
        log.debug("name2: "+ name2);


        // HttpServletRequest의 헤더 정보 로그
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.debug("Request Header: " + headerName + " = " + headerValue);
        }

//         HttpServletResponse의 헤더 정보 로그
        Collection<String> headerNamesResponse = response.getHeaderNames();
        for (String headerName : headerNamesResponse) {
            String headerValue = response.getHeader(headerName);
            log.debug("Response Header: " + headerName + " = " + headerValue);
        }

        String userNickname = request.getHeader("userNickname");
        if (userNickname != null) {
            try {
                userNickname = URLDecoder.decode(userNickname, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // 디코딩 실패시 예외 처리
                e.printStackTrace();
            }
        }
        log.debug("시작");
//        String userNickname = request.getHeader("userNickname");
        log.debug("userNickname: "+ userNickname);
        log.debug("userId: "+ request.getHeader("userId")+" userNickname: "+request.getHeader("userNickname"));
        return "WELCOME";
    }

    @PostMapping("/signup")
    public BasicResponse signUp(@RequestBody SignUpRequestDto dto){

        log.debug("회원가입 : "+ dto.getUserLoginId());
        authService.signUp(dto);
        return new BasicResponse(HttpStatus.OK.value(), "회원가입 성공");

    }

    @PostMapping("/signin")
    public BasicResponse signIn(@RequestBody SignInRequestDto dto){

        log.debug("로그인 : " + dto.getUserLoginId());
        return new BasicResponse(HttpStatus.OK.value(), authService.signIn(dto));
    }

    @PostMapping("/reissue")
    public BasicResponse reissueToken(HttpServletRequest request){

        log.debug("Access Token 재발급: "+ request);
        return new BasicResponse(HttpStatus.OK.value(), authService.reissueToken(request));
    }

}
