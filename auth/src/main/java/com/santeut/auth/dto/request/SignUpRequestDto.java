package com.santeut.auth.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String userNickname;
    private String userLoginId;
    private String userPassword;
    @Column(length = 6)
    private String userBirth;
    private char userGender;
}
