package com.pawn.wantedcqrs.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pawn.wantedcqrs.review.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Long id;
    private Long productId;
    private Long userId;
    private Integer rating;
    private String title;
    private String content;
    private Boolean verifiedPurchase ;
    private Integer helpfulVotes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;


    public Review toEntity() {
        return Review.builder()
                .id(id)
                .productId(productId)
                .userId(userId)
                .rating(rating)
                .title(title)
                .content(content)
                .verifiedPurchase(verifiedPurchase)
                .helpfulVotes(helpfulVotes)
                .build();
    }

    public static ReviewDto fromEntity(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setProductId(review.getProductId());
        dto.setUserId(review.getUserId());
        dto.setRating(review.getRating());
        dto.setTitle(review.getTitle());
        dto.setContent(review.getContent());
        dto.setVerifiedPurchase(review.getVerifiedPurchase());
        dto.setHelpfulVotes(review.getHelpfulVotes());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }

}
