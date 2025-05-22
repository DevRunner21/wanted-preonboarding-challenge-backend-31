package com.pawn.wantedcqrs.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewQueryCondition {

    private final Integer rating;

    private final Long productId;

    @Builder
    protected ReviewQueryCondition(Integer rating, Long productId) {
        this.rating = rating;
        this.productId = productId;
    }

}
