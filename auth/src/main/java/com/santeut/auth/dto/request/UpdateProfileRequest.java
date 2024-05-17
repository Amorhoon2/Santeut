package com.santeut.auth.dto.request;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateProfileRequest {

    @Nullable
    String userNickname;
    @Nullable
    String currentPassword;
    @Nullable
    String updatePassword;
}
