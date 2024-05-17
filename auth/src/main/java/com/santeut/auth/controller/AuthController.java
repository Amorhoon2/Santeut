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
import com.deepl.api.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    Translator translator;

    @GetMapping("/welcome")
    public String welcome() throws DeepLException, InterruptedException {

        String authKey = "897064a0-60a9-45ac-a0b8-2bc9e8ec0837:fx";
        translator = new Translator(authKey);
        String text = "Prunus cerasifera is a species of plum known by the common names cherry plum and myrobalan plum. It is native to Southeast Europe and Western Asia, and is naturalised in the British Isles and scattered locations in North America. Also naturalized in parts of SE Australia where it is considered to be a mildly invasive weed of bushland near urban centers.";
        TextResult result = translator.translateText(text, null, "ko");
        return result.getText();
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
