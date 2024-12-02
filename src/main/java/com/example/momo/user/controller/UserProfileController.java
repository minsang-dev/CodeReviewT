package com.example.momo.user.controller;

import com.example.momo.user.dto.profile.ProfileResponseDto;
import com.example.momo.user.dto.profile.ProfileUpdateRequestDto;
import com.example.momo.user.dto.profile.ProfileUpdateResponseDto;
import com.example.momo.user.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @RestController : 웹 서비스를 구현하기 위한 컨트롤러 [JSON 형식으로 데이터를 반환하며, HTTP 요청과 응답을 처리함]
 * @RequestMapping("/api/users/profiles") : 기본경로, profile 호출
 * @RequiredArgsConstructor : final 필드에 대한 생성자 Lombok이 자동 생성,
 * 코드의 가독성을 높이고, DI로 Service 계층의 생성을 스프링 프레임워크가 담당함.
 */
@RestController
@RequestMapping("/api/users/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // 프로필 조회
    @GetMapping("/api/users/profiles/{userId}") // {userId}는 경로 변수, 클라이언트 요청 url
    public ResponseEntity<ProfileResponseDto> findUserProfile(@PathVariable Long userId) {

        /**
         * userProfileService.findUserProfile(userId)를 호출하여 비즈니스 로직 실행하여 프로필 조회를 함
         * ProfileResponseDto 객체에 결과 반환 데이터를 저장.
         */
        ProfileResponseDto responseDto = userProfileService.findUserProfile(userId);

        /**
         * 메서드 응답 반환, 상태코드(HttpStatus.CREATED)와 데이터(responseDto)를 함께 전달하기 위해 ResponseEntity 사용
         */
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/api/user/profiles") // 클라이언트 요청 url

    /**
     * 프로필 수정
     */
    public ResponseEntity<ProfileUpdateResponseDto> updateUserProfile(
            HttpServletRequest request, // 요청 헤더, 세션 정보, 클라이언트의 IP 주소 등 추가적인 요청 정보 처리
            @RequestBody ProfileUpdateRequestDto requestDto // json 데이터를 JAVA 객체로 매핑함
    ){
        /**
         * userProfileService.updateUserProfile 메서드를 호출하여 로직 실행 후, 결과를 ProfileUpdateResponseDto 객체에 저장함.
         */
        ProfileUpdateResponseDto responseDto = userProfileService.updateUserProfile(
                request,
                requestDto.getUserName(),
                requestDto.getProfileImageUrl(),
                requestDto.getPassword()
        );

        /**
         * 응답 데이터와 Http 상태코드도 함께 전달.
         * responseDto는 사용자 프로필 업데이트 결과를 포함하는 객체
         */
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
