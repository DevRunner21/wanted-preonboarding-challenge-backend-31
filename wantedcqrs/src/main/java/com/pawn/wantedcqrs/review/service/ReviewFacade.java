package com.pawn.wantedcqrs.review.service;

import com.pawn.wantedcqrs.product.dto.ProductDto;
import com.pawn.wantedcqrs.product.service.ProductService;
import com.pawn.wantedcqrs.review.dto.ReadReviewsResponse;
import com.pawn.wantedcqrs.review.dto.ReviewDto;
import com.pawn.wantedcqrs.review.dto.ReviewQueryCondition;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;
import com.pawn.wantedcqrs.user.dto.UserDto;
import com.pawn.wantedcqrs.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewFacade {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final UserService userService;

    private final ReviewAssembler reviewAssembler;


    public ReadReviewsResponse readProductReviewPage(Long productId, ReviewQueryCondition condition, Pageable pageable) {
        // 상품 Validation Check
        ProductDto foundProduct = productService.getProductBy(productId);

        // 상품 리뷰 Summary 조회
        Map<Long, ReviewStatsProjection> reviewStatsMap = reviewService.getReviewStatsMapBy(List.of(productId));
        ReviewStatsProjection reviewStats = reviewStatsMap.get(productId);

        ReviewDistributionProjection distribution = reviewService.getDistributionBy(productId);

        // 상품 리뷰 목록 조회 (페이징)
        Page<ReadReviewsResponse.ReviewResult> reviewResultPage = getReviewPageBy(condition, pageable);

        // ReviewStatsProjection, ReviewDistributionProjection, Page<ReadReviewsResponse.ReviewResult>를 합쳐서 ReadReviewsResponse 반환
        return reviewAssembler.assemble(reviewResultPage, reviewStats, distribution);
    }

    public Page<ReadReviewsResponse.ReviewResult> getReviewPageBy(ReviewQueryCondition condition, Pageable pageable) {

        Page<ReviewDto> reviewPage = reviewService.getReviewPageBy(condition, pageable);

        // 사용자 정보 조회
        List<Long> userIds = reviewPage.getContent().stream().map(ReviewDto::getUserId).toList();
        Map<Long, UserDto> usersMap = userService.getUsersMapBy(userIds);

        // UserDto와 ReviewDto를 합쳐서 Page<ReadReviewsResponse.ReviewResult>를 반환
        return reviewAssembler.toReviewResultPage(reviewPage, usersMap);
    }


}
