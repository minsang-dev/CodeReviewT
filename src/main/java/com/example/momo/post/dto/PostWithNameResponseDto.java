package com.example.momo.post.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostWithNameResponseDto {

    private final Long postId;
    private final String userName;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostWithNameResponseDto(Long postId, String userName, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postId = postId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
