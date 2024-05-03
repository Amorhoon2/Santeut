package com.santeut.auth.controller;


import com.santeut.auth.common.response.BasicResponse;
import com.santeut.auth.dto.request.SignInRequestDto;
import com.santeut.auth.dto.request.SignUpRequestDto;
import com.santeut.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/welcome")
    public String welcome(){
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
