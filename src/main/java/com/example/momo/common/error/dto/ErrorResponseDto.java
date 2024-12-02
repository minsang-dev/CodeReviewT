package com.example.momo.common.error.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDto {

    private int errorCode; //HTTP 상태 코드
    private String message; // 에러 메시지
    private String path; // 요청 URL
    private LocalDateTime errorTime; // 에러 발생 시간
}