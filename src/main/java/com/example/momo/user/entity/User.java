package com.example.momo.user.entity;

import com.example.momo.common.entity.TimeBaseEntity;
import com.example.momo.friend.entity.Friend;
import com.example.momo.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user") // 테이블 매핑 이름
public class User extends TimeBaseEntity { // createdAt, updatedAt 필드 포함시킴

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키가 데이터베이스에서 자동으로 생성되도록 설정
    private Long id; // 유저 고유 식별자

    @Column(nullable = false)
    private String name; // 유저명

    @Column(nullable = false, unique = true)
    private String email; // 유저 이메일

    private String profileImageUrl; // 유저 프로필 이미지 링크

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private Boolean isDeleted = false; // 1인 경우 true, 0인 경우 false

    /**
     * user와 post 엔티티 간의 1:N 관계
     * mappedBy = "user" : Post 엔티티의 user필드가 관계의 주인
     * cascade = CascadeType.ALL : 부모 엔티티에서 수행되는 모든 작업이 자식 엔티티에도 전파됨
     * orphanRemoval = true : 부모와 관계가 끊어진 자식 엔티티를 자동으로 삭제
     */

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // 사용자가 작성한 게시글 목록 저장

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> sentFriendRequests = new ArrayList<>(); // 내가 보낸 친구 요청

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> receivedFriendRequests = new ArrayList<>(); // 내가 받은 친구 요청

    public User(String name, String email, String profileImageUrl, String encode) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.password = encode;
    }

    public User() {}
}
