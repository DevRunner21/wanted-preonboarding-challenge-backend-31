package com.pawn.wantedcqrs.review.repository;

import com.pawn.wantedcqrs.review.dto.ReviewQueryCondition;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.entity.Review;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface QReviewRepositoryCustom {

    List<ReviewStatsProjection> findReviewStatsByProductIds(Collection<Long> productIds);

    ReviewDistributionProjection findDistributionBy(Long productId);

    Page<Review> findReviewPageBy(ReviewQueryCondition condition, Pageable pageable);

}
