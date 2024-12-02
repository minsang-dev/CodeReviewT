package com.example.momo.user.service;

import com.example.momo.common.config.PasswordEncoder;
import com.example.momo.user.dto.profile.ProfileResponseDto;
import com.example.momo.user.dto.profile.ProfileUpdateResponseDto;
import com.example.momo.user.entity.User;
import com.example.momo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service // 서비스 계층 명시, 다른 컴포넌트에서 의존성 주입으로 사용됨
@RequiredArgsConstructor // final로 선언된 필드에 대한 생성자를 자동으로 생성하기 위해 사용.
public class UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * @param userId // 조회
     * @return
     */
    public ProfileResponseDto findUserProfile(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(null); // 사용자 없을 경우에 예외 던지기 위함.

        return new ProfileResponseDto(
                findUser.getId(),
                findUser.getName(),
                findUser.getEmail(),
                findUser.getProfileImageUrl()
        );
    }

    public ProfileUpdateResponseDto updateUserProfile(HttpServletRequest request, String userName, String profileImageUrl, String rawPassword) {
        HttpSession session = request.getSession(false); // 세션 o -> 현재 로그인된 사용자의 httpServletRequest를 가져옴

        Long currentUserId = (Long) session.getAttribute("userId"); // 현재 로그인하고 있는 사용자의 정보를 가져옴

        User updateUser = userRepository.findById(currentUserId).orElseThrow(null); // user 찾아옴

        if (!passwordEncoder.matches(rawPassword, updateUser.getPassword())) { // 비밀번호 인코딩 일치하는지 ?
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // 수정
        userRepository.save(updateUser);

        return new ProfileUpdateResponseDto(
                updateUser.getId(),
                updateUser.getName(),
                updateUser.getProfileImageUrl(),
                updateUser.getModifiedAt()
        );
    }
}
