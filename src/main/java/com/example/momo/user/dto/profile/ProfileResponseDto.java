package com.example.momo.user.dto.profile;

import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private final Long userId;
    private final String userName;
    private final String userEmail;
    private final String profileImageUrl;

    public ProfileResponseDto(Long userId, String userName, String userEmail, String profileImageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.profileImageUrl = profileImageUrl;
    }
}
