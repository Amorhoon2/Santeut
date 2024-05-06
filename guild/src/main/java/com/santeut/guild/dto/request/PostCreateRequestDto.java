package com.santeut.guild.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequestDto {

    private int guildId;

    private String type;

    private String title;

    private String content;

    private List<Map<String, String>> photos;
}
