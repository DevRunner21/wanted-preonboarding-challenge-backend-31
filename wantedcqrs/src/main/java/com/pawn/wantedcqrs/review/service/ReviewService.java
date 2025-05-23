package com.pawn.wantedcqrs.review.service;

import com.pawn.wantedcqrs.common.exception.e4xx.ForbiddenException;
import com.pawn.wantedcqrs.common.exception.e4xx.ResourceNotFoundException;
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
import java.util.Objects;
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

    @Transactional
    public ReviewDto save(ReviewDto reviewDto) {
        Review savedReview = reviewRepository.save(reviewDto.toEntity());

        return ReviewDto.fromEntity(savedReview);
    }

    @Transactional
    public ReviewDto update(Long id, ReviewDto reviewDto) {
        Review origin = reviewRepository.findById(id).orElseThrow(ResourceNotFoundException.REVIEW::getResponseException);

        // 수정 권한 체크
        if(!Objects.equals(reviewDto.getUserId(), origin.getUserId())) {
            throw ForbiddenException.FORBIDDEN.getResponseException();
        }

        Review updatedReview = reviewRepository.save(origin.update(reviewDto));

        return ReviewDto.fromEntity(updatedReview);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        Review target = reviewRepository.findById(id).orElseThrow(ResourceNotFoundException.REVIEW::getResponseException);

        // 삭제 권한 체크
        if(!Objects.equals(userId, target.getUserId())) {
            throw ForbiddenException.FORBIDDEN.getResponseException();
        }

        reviewRepository.delete(target);
    }

}
