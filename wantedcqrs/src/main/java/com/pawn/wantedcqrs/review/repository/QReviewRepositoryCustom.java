package com.pawn.wantedcqrs.review.repository;

import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;

import java.util.Collection;
import java.util.List;

public interface QReviewRepositoryCustom {

    List<ReviewStatsProjection> findReviewStatsByProductIds(Collection<Long> productIds);

    ReviewDistributionProjection findDistributionBy(Long productId);

}
