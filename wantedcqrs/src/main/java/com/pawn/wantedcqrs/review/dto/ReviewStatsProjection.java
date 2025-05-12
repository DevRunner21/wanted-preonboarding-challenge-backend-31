package com.pawn.wantedcqrs.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ReviewStatsProjection {
    private final Long productId;
    private final Double rating;
    private final Long reviewCount;

    @QueryProjection
    public ReviewStatsProjection(Long productId, Double rating, Long reviewCount) {
        this.productId = productId;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

}
