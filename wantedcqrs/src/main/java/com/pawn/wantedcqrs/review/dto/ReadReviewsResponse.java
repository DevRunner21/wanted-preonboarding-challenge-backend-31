package com.pawn.wantedcqrs.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ReadReviewsResponse {

    private List<ReviewResult> items;
    private Summary summary;
    private Pagination pagination;

    @Getter
    @Builder
    public static class ReviewResult {
        private Long id;
        private User user;
        private Integer rating;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean verifiedPurchase;
        private Integer helpfulVotes;

        @Getter
        @Builder
        public static class User {
            private Long id;
            private String name;
            private String avatarUrl;
        }

    }

    @Getter
    @Builder
    public static class Summary {
        private Double averageRating;
        private Integer totalCount;
        private Map<Integer, Integer> distribution; // key: 평점 (1~5), value: 개수
    }

    @Getter
    @Builder
    public static class Pagination {
        private Integer totalItems;
        private Integer totalPages;
        private Integer currentPage;
        private Integer perPage;
    }

}