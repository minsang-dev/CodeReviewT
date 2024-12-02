package com.example.momo.post.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
public class PostPageResponseDto {

    private final List<PostWithNameResponseDto> post;
    private final PageInfo pageInfo;

    public PostPageResponseDto(List<PostWithNameResponseDto> post, PageInfo pageInfo) {
        this.post = post;
        this.pageInfo = pageInfo;
    }

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private int total;
        private int size;
        private int totalElements;
        private int totalPages;
    }
}
