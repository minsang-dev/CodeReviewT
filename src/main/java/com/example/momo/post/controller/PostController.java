package com.example.momo.post.controller;

import com.example.momo.post.dto.*;
import com.example.momo.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor

public class PostController {

    // PostService 의존성 주입
    private final PostService postService;

    // 게시글 생성 POST 요청
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            HttpServletRequest request, // 사용자 인증
            @Valid @RequestBody PostRequestDto requestDto // 객체 매핑, 유효성 검사 수행
    ) {
        PostResponseDto responseDto = postService.createPost( // 게시글 생성 로직 처리
                request,
                requestDto.getTitle(),
                requestDto.getContent()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED); // 응답 데이터와 성공 상태코드 반환
    }

    // 페이징 처리(게시글 목록 조회) GET 요청
    @GetMapping
    public ResponseEntity<PostPageResponseDto> getPostPaginated( // 목록 데이터 반환
            @RequestParam(defaultValue = "1") int page, // page 쿼리 파라미터 읽고 기본값 1로 설정
            @RequestParam(defaultValue = "10") int size // size 쿼리 파라미터 읽고 기본값 1로 설정
    ) {
        // 0부터 시작하는 페이지 번호로 변환 -> 페이징 처리된 게시글 목록 데이터 가져옴
        PostPageResponseDto pageResponseDto = postService.getPostsPaginated(page -1, size);

        return new ResponseEntity<>(pageResponseDto, HttpStatus.OK); // 응답 데이터와 성공 상태코드 반환
    }

    // 게시글 수정 PATCH 요청
    @PatchMapping("/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost( // 게시글 수정 요청에 대한 응답 데이터 반환
            HttpServletRequest request,
            @PathVariable Long postId,// url 경로에서 postId 값 추출하여 매개변수로 전달함
            @Valid @RequestBody PostUpdateRequestDto requestDto // 요청 데이터 매핑하여 유효성 검사 수행
    ) {
        PostUpdateResponseDto responseDto = postService.updatePost( // 수정할 내용 전달
                request,
                postId,
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getPassword()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK); // 응답 데이터와 성공 상태코드 반환
    }

    // 게시글 삭제 DELETE 요청
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost( // 게시글 삭제 요청에 대한 응답 반환 메서드, void:데이터 반환x
            HttpServletRequest request,
            @PathVariable Long postId, // url 경로에서 삭제할 게시글의 ID 추출하여 메서드 매개변수로 전달
            @Valid @RequestBody PostDeleteRequestDto requestDto // 매핑 후 유효성 검사 수행
    ) {
        postService.deletePost(request, postId, requestDto.getPassword()); // 삭제할 게시글 ID, 비밀번호 전달

        return new ResponseEntity<>(HttpStatus.OK); // 응답 데이터와 성공 상태코드 반환
    }

}
