package com.santeut.auth.common.response;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String name;
    private String message;

    public ErrorResponse(int status, String message){
        this.status = status;
        this.message = message;
    }
}
