package com.pawn.wantedcqrs.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateReviewResponse {
    private Long id;
    private CreateReviewResponse.User user;
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
