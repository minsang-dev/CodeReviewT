package com.example.momo.user.repository;

import com.example.momo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface UserRepository extends JpaRepository<User, Long> {

    // email로 사용자 검색하는 메서드
    User findByEmail(String email);

    // email과 isDeleted조건 만족하는 사용자 데이터 검색
    User findByEmailAndIsDeleted(String email, Boolean isDeleted);

    // id로 사용자 검색 -> 사용자 x -> 예외 발생
    default User findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found")
                        );
    }
}
