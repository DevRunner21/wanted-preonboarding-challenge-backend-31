package com.pawn.wantedcqrs.review.controller;

import com.pawn.wantedcqrs.common.dto.response.CommonApiResponse;
import com.pawn.wantedcqrs.common.util.PageableCreator;
import com.pawn.wantedcqrs.review.dto.CreateReviewRequest;
import com.pawn.wantedcqrs.review.dto.CreateReviewResponse;
import com.pawn.wantedcqrs.review.dto.ReadReviewsResponse;
import com.pawn.wantedcqrs.review.dto.ReviewQueryCondition;
import com.pawn.wantedcqrs.review.service.ReviewFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewFacade reviewFacade;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<CommonApiResponse<ReadReviewsResponse>> readProductReviewPages(
            @PathVariable Long productId,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "created_at:desc") List<String> sort,
            @RequestParam(name = "rating", required = false) Integer rating) {
        ReviewQueryCondition condition = ReviewQueryCondition.builder()
                .productId(productId)
                .rating(rating)
                .build();

        Pageable pageable = PageableCreator.create(page, perPage, sort);

        ReadReviewsResponse response = reviewFacade.readProductReviewPage(productId, condition, pageable);

        return ResponseEntity.ok(CommonApiResponse.ok(response));
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<CommonApiResponse<CreateReviewResponse>> create(
            @PathVariable Long productId
            , @RequestBody CreateReviewRequest request
    ) {

        Long userId = 1L; // TODO: 인증인가 붙이면서 변경 예정

        CreateReviewResponse response = reviewFacade.save(productId, userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).
                body(CommonApiResponse.ok(response));
    }

}
