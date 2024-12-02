package com.example.momo.user.service;

import com.example.momo.common.config.PasswordEncoder;
import com.example.momo.common.util.UtilValidation;
import com.example.momo.user.dto.login.LoginRequestDto;
import com.example.momo.user.dto.signUp.SignUpResponseDto;
import com.example.momo.user.entity.User;
import com.example.momo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 회원 가입
    public SignUpResponseDto signUp(
            String name,
            String email,
            String profileImageUrl,
            String password
    ) {

        // Util package -> UtilValidation: 비밀번호 충족 요건 검사
        if (!UtilValidation.isValidPasswordFormat(password)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, // 비밀번호 유효 x -> 예외 발생 (403코드)
                    "비밀번호는 최소 8자,  대소문자 포함한 영문, 숫자, 특수문자를 포함해야합니다.");
        }

        // 새로운 사용자 객체 생성, 비밀번호 암호화
        User user = new User(name, email, profileImageUrl, passwordEncoder.encode(password));

        // 해당 이메일 가진 사용자 조회
        User userByEmail = userRepository.findByEmail(email);

        // 동일한 email 존재 시 예외 발생
        if (userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "중복된 이메일입니다.");
        }

        // 새로운 사용자 정보 저장
        User savedUser = userRepository.save(user);

        // 저장된 사용자 정보 반환
        return new SignUpResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getProfileImageUrl(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt());
    }

    // 사용자 로그인 처리
    public void loginUser(HttpServletRequest request, LoginRequestDto loginRequestDto) {

        // 이메일로 데이터 조회 -> false : 삭제된 사용자는 로그인 할 수 없음 (isDeleted = true)
        User user = userRepository.findByEmailAndIsDeleted(loginRequestDto.getEmail(), false);

        // 사용자 존재하지 않을 경우, 예외 발생 (401코드)
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자입니다.");
        }

        // 사용자가 입력한 비밀번호와 데이터베이스에 저장된 암호화된 비밀번호 비교, 일치 x -> 예외 발생
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 비밀번호입니다.");
        }

        // 현재 HTTP 요청에서 세션 가져옴, 세션 없으면 새로 생성
        HttpSession session = request.getSession();
        // 로그인 성공 시 세션에 사용자 ID 저장
        session.setAttribute("userId", user.getId());
    }

    @Transactional // 사용자 계정 삭제 로직 처리
    public void deleteUser(HttpServletRequest request, Long id, String password) {

        // 사용자 ID로 데이터 조회, 존재하지 않을 경우 예외 발생
        User deleteUser = userRepository.findByIdOrElseThrow(id);

        // 세션이 이미 존재하는 경우, 세션 가져옴
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId"); // 현재 로그인한 사용자의 ID 가져옴

        // 로그인한 사용자의 ID와 삭제하려는 사용자 ID가 일치하지 않으면 예외 발생
        if (!userId.equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "다른 사용자의 계정은 삭제 할 수 없습니다.");
        }

        // 입력한 비밀번호와 데이터베이스에 저장된 비밀번호와 일치하는지 검사
        if (passwordEncoder.matches(password, deleteUser.getPassword())) {
            deleteUser.setIsDeleted(true); // 사용자 계정 -> 삭제
            userRepository.delete(deleteUser); // 데이터베이스에서 사용자 삭제
        } else { // 비밀번호 일치하지 않으면 예외 발생 (403코드)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }
    }
}
