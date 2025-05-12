package com.pawn.wantedcqrs.review.repository;

import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;

import java.util.Collection;
import java.util.List;

public interface QReviewRepositoryCustom {

    List<ReviewStatsProjection> findReviewStatsByProductIds(Collection<Long> productIds);

}
