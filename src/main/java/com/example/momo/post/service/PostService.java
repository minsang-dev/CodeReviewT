package com.example.momo.post.service;

import com.example.momo.common.config.PasswordEncoder;
import com.example.momo.post.dto.PostPageResponseDto;
import com.example.momo.post.dto.PostResponseDto;
import com.example.momo.post.dto.PostUpdateResponseDto;
import com.example.momo.post.dto.PostWithNameResponseDto;
import com.example.momo.post.entity.Post;
import com.example.momo.post.repository.PostRepository;
import com.example.momo.user.entity.User;
import com.example.momo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 게시글 생성 요청 처리 메서드
    public PostResponseDto createPost(HttpServletRequest request, String title, String content) {

        /**
         * 1. 현재 클라이언트 session 가져옴 (false로 설정해 session이 없으면 생성하지 않음)
         * 2. session에 저장된 userId 가져와서 사용자가 누구인지 확인함
         * 3. userId를 통해 게시글 작성자 정보 가져옴
         * 4. 입력받은 제목, 내용을 포함한 새로운 Post 객체 생성해서 savedPost에 저장
         */

        HttpSession session = request.getSession(false); //1

        Long userId = (Long) session.getAttribute("userId"); //2

        User postUser = userRepository.findByIdOrElseThrow(userId); //3
        Post savedPost = postRepository.save(new Post(title, content, postUser)); //4

        return new PostResponseDto( // 게시글 생성에 대한 응답 데이터 반환.
                savedPost.getId(),
                postUser.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreatedAt()
        );
    }

// 게시글 페이징 처리 -> 게시글 목록 반환하는 메서드
    public PostPageResponseDto getPostsPaginated(int page, int size) {

        // 요청한 페이지와 페이지 크기, 내림차순으로 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 삭제되지 않은 사용자가 작성한 게시글만 페이징 처리
        Page<Post> postPage = postRepository.findAllByUserIsDeletedIsFalse(pageable);

        // Post 데이터를 클라이언트에 필요한 형태의 DTO로 변환
        Page<PostWithNameResponseDto> posts = postPage.map(post -> new PostWithNameResponseDto(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        ));

        // 페이지 정보를 담은 객체 생성
        PostPageResponseDto.PageInfo pageInfo = new PostPageResponseDto.PageInfo(
                posts.getNumber() + 1,
                posts.getSize(),
                (int) posts.getTotalElements(),
                posts.getTotalPages()
        );

        // 게시글 응답 데이터, 페이지 정보 반환
        return new PostPageResponseDto(posts.getContent(), pageInfo);
    }

    // 게시글 수정 요청 처리
    public PostUpdateResponseDto updatePost(
            HttpServletRequest request, Long postId, String title, String content, String password) { //

        // 사용자의 게시글 접근 권한, 비밀번호 검증, 검증에 성공하면 수정할 Post 객체 반환
        Post post = validateUserAcess(request, postId, password);

        post.setTitle(title);
        post.setContent(content);
        Post updatedPost = postRepository.save(post); // 제목, 내용 수정하여 updatedPost에 저장

        return new PostUpdateResponseDto(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getModifiedAt()
        );
    }

    // 게시글 삭제 요청 처리
    public void deletePost(HttpServletRequest request, Long postId, String password) {
        Post post = validateUserAcess(request, postId, password); // 게시글 접근 권한, 비밀번호 검증

        postRepository.delete(post); // 해당 게시글 데이터 삭제
    }

    // 게시글 수정, 삭제 권한 및 비밀번호 검증 후 Post 객체 반환
    private Post validateUserAcess(HttpServletRequest request, Long postId, String password) {

        HttpSession session = request.getSession(false); // 현재 session 정보 가져옴
        Long userId = (Long) session.getAttribute("userId"); // 요청 보낸 사용자의 ID 가져옴

        Post post = postRepository.findByIdOrElseThrow(postId); // 게시글 ID로 해당 게시글 조회
        User postUser = post.getUser(); // 게시글 작성자 확인
        
        // 요청을 보낸 사용자와 게시글 작성자가 일치하지 않을 경우 예외 던짐
        if(!userId.equals(postUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 게시물만 수정이 가능합니다.");
        }
        
        // 입력받은 비밀번호가 사용자 계정의 암호화된 비밀번호와 일치하는 확인
        if(!passwordEncoder.matches(password, postUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }
        
        // 검증 성공하면 해당 게시글 객체 반환
        return post;
    }


}
