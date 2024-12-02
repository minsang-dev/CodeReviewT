package com.example.momo.user.controller;

import com.example.momo.user.dto.delete.UserDeleteRequestDto;
import com.example.momo.user.dto.login.LoginRequestDto;
import com.example.momo.user.dto.signUp.SignUpRequestDto;
import com.example.momo.user.dto.signUp.SignUpResponseDto;
import com.example.momo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup") // 경로: /api/users/signup
    public ResponseEntity<SignUpResponseDto> signUp(  // 회원 가입 요청 메서드
            @RequestBody SignUpRequestDto requestDto // Json 데이터를 객체로 매핑
    ) {
        SignUpResponseDto signUpResponseDto =
                userService.signUp( //UserService 호출하여 회원가입 로직 처리
                        requestDto.getName(),
                        requestDto.getEmail(),
                        requestDto.getProfileImageUrl(),
                        requestDto.getPassword()
                );
        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED); // 생성된 사용자 정보, 상태코드 반환
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser( // 로그인 요청 처리 메서드
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request
    ) {
        userService.loginUser(request, loginRequestDto); // UserService를 호출해 로그인 로직 처리
        // return new ResponseEntity<>(loginRequestDto.getEmail(), HttpStatus.OK);
        // return new ResponseEntity<>(HttpStatus.OK);
        return ResponseEntity.ok().body("정상적으로 로그인되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {

        HttpSession session = request.getSession(false); // ??? false 설정해 세션이 없을 경우, 새로 생성하지 않음

        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 성공입니다."); // 성공 메시지, 상태코드 반환
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser( // 사용자 삭제 요청 처리 메서드
            @PathVariable Long id, // 요청 경로에서 사용자 ID 주출
            @RequestBody UserDeleteRequestDto requestDto, // 삭제 요청 정보 매핑
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false); // 현재 세션 가져와서 삭제 후 세션을 무효화시킴
        // UserService 호출해 사용자 삭제 로직 처리 (사용자 ID, 비밀번호)
        userService.deleteUser(request, id, requestDto.getPassword());
        session.invalidate(); // ??????? 세션 무효화하여 사용자 로그아웃하도록 -
        return ResponseEntity.ok().body("정상적으로 삭제되었습니다.");
    }

}
