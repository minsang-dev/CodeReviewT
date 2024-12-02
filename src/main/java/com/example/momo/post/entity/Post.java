package com.example.momo.post.entity;

import com.example.momo.common.entity.TimeBaseEntity;
import com.example.momo.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "post") // 테이블 이름 매핑
public class Post extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 고유 식별자

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저 (ManyToOne 관계)

    public Post() {}

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
