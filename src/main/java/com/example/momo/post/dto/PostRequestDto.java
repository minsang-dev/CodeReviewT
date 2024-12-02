package com.example.momo.post.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostRequestDto {

    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;


    public PostRequestDto(Long id, Long userId, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
