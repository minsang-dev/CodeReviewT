package com.example.momo.user.dto.signUp;

import java.time.LocalDateTime;

public class SignUpResponseDto {

    private final Long id;
    private final String name;
    private final String eamail;
    private final String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SignUpResponseDto(Long id, String name, String eamail, String profileImageUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.eamail = eamail;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
