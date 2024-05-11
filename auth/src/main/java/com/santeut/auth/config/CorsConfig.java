package com.santeut.auth.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {

    private final static String[] ALLOWED_ORIGINS = {
            "http://localhost:52711",
            "https://k10e201.p.ssafy.io"
    };

    private final static String[] ALLOWED_METHODS = {
            "GET", "POST", "PATCH", "PUT", "DELETE"
    };

    public void addCorsMappings(CorsRegistry registry){

        registry.addMapping("/**")
                .allowedOrigins(ALLOWED_ORIGINS)
                .allowedMethods(ALLOWED_METHODS)
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(300);
    }
}
