package com.pawn.wantedcqrs.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateReviewRequest {

    private final Integer rating;
    private final String title;
    private final String content;

    public ReviewDto toReviewDto(Long productId, Long userId) {
        ReviewDto dto = new ReviewDto();
        dto.setProductId(productId);
        dto.setUserId(userId);
        dto.setRating(this.rating);
        dto.setTitle(this.title);
        dto.setContent(this.content);
        return dto;
    }

}
