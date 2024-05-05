package com.santeut.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String userNickname;
    private String userLoginId;
    private String userPassword;
    private String userBirth;
    private char userGender;
}
