package com.santeut.auth.dto.request;

import lombok.Getter;

@Getter
public class HikingRecordRequest {

    private int userId;
    private int distance;
    private int moveTime;
    private Boolean isFirstMountain;
}
