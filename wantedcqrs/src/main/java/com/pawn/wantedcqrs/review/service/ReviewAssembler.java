package com.pawn.wantedcqrs.review.service;

import com.pawn.wantedcqrs.review.dto.CreateReviewResponse;
import com.pawn.wantedcqrs.review.dto.ReadReviewsResponse;
import com.pawn.wantedcqrs.review.dto.ReviewDto;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;
import com.pawn.wantedcqrs.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReviewAssembler {

    public ReadReviewsResponse toReadReviewsResponse(
            Page<ReadReviewsResponse.ReviewResult> reviewPage,
            ReviewStatsProjection stats,
            ReviewDistributionProjection distribution
    ) {
        return ReadReviewsResponse.builder()
                .items(reviewPage.getContent())
                .summary(toSummary(stats, distribution))
                .pagination(toPagination(reviewPage))
                .build();
    }

    private ReadReviewsResponse.Summary toSummary(
            ReviewStatsProjection stats,
            ReviewDistributionProjection distribution
    ) {
        Map<Integer, Integer> distMap = new HashMap<>();
        distMap.put(5, distribution.five().intValue());
        distMap.put(4, distribution.four().intValue());
        distMap.put(3, distribution.three().intValue());
        distMap.put(2, distribution.two().intValue());
        distMap.put(1, distribution.one().intValue());

        return ReadReviewsResponse.Summary.builder()
                .averageRating(stats.getRating())
                .totalCount(stats.getReviewCount().intValue())
                .distribution(distMap)
                .build();
    }

    private ReadReviewsResponse.Pagination toPagination(Page<?> page) {
        return ReadReviewsResponse.Pagination.builder()
                .totalItems((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber() + 1) // 0-based index → 1-based index
                .perPage(page.getSize())
                .build();
    }

    public Page<ReadReviewsResponse.ReviewResult> toReviewResultPage(
            Page<ReviewDto> reviewPage,
            Map<Long, UserDto> userMap
    ) {
        return reviewPage.map(review -> {
            UserDto user = userMap.get(review.getUserId());

            return ReadReviewsResponse.ReviewResult.builder()
                    .id(review.getId())
                    .user(ReadReviewsResponse.ReviewResult.User.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .avatarUrl(user.getAvatarUrl())
                            .build())
                    .rating(review.getRating())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .verifiedPurchase(review.getVerifiedPurchase())
                    .helpfulVotes(review.getHelpfulVotes())
                    .build();
        });
    }

    public CreateReviewResponse toCreateReviewResponse(ReviewDto review, UserDto user) {
        return CreateReviewResponse.builder()
                .id(review.getId())
                .user(CreateReviewResponse.User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .avatarUrl(user.getAvatarUrl())
                        .build())
                .rating(review.getRating())
                .title(review.getTitle())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .verifiedPurchase(review.getVerifiedPurchase())
                .helpfulVotes(review.getHelpfulVotes())
                .build();
    }

}
