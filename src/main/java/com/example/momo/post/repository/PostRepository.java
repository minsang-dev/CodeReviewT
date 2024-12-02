package com.example.momo.post.repository;

import com.example.momo.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Post 엔티티를 조회하면서, 연관된 User엔티티가 삭제되지 않ㅇ은 경우에만 데이터 반환
    Page<Post> findAllByUserIsDeletedIsFalse(Pageable pageable);

    // 특정 postId에 해당하는 entity 조회하거나, 해당 entity가 없을 경우 예외를 던지기 위한 메사드로 사용됨.
    Post findByIdOrElseThrow(Long postId);
}
