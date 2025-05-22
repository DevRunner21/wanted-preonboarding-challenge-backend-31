package com.pawn.wantedcqrs.review.service;

import com.pawn.wantedcqrs.review.dto.ReviewDto;
import com.pawn.wantedcqrs.review.dto.ReviewQueryCondition;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.entity.Review;
import com.pawn.wantedcqrs.review.repository.ReviewRepository;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Map<Long, ReviewStatsProjection> getReviewStatsMapBy(List<Long> productIds) {
        return reviewRepository.findReviewStatsByProductIds(productIds).stream()
                .collect(Collectors.toMap(
                        ReviewStatsProjection::getProductId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    public ReviewDistributionProjection getDistributionBy(Long productId) {
        return reviewRepository.findDistributionBy(productId);
    }

    public Page<ReviewDto> getReviewPageBy(ReviewQueryCondition condition, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findReviewPageBy(condition, pageable);
        List<ReviewDto> contents = reviewPage.getContent().stream().map(ReviewDto::fromEntity).toList();

        return new PageImpl<>(contents, pageable, reviewPage.getTotalElements());
    }

}
