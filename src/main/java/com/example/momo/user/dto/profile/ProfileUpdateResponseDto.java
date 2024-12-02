package com.example.momo.user.dto.profile;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProfileUpdateResponseDto{
    private final Long userId;
    private final String userName;
    private final String profileImageUrl;
    private final LocalDateTime modifiedAt;

    public ProfileUpdateResponseDto(Long userId, String userName, String profileImageUrl, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.modifiedAt = modifiedAt;
    }
}