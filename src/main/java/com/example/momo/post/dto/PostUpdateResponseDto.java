package com.example.momo.post.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostUpdateResponseDto {

    private final Long postId;
    private final String title;
    private final String content;
    private final LocalDateTime updatedAt;

    public PostUpdateResponseDto(Long postId, String title, String content, LocalDateTime updatedAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
