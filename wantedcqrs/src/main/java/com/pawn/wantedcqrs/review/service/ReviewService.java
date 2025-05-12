package com.pawn.wantedcqrs.review.service;

import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
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

}
