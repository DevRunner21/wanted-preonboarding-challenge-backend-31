package com.pawn.wantedcqrs.review.repository;

import com.pawn.wantedcqrs.review.dto.QReviewStatsProjection;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.pawn.wantedcqrs.review.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class QReviewRepositoryCustomImpl implements QReviewRepositoryCustom{

    private final JPAQueryFactory qf;

    @Override
    public List<ReviewStatsProjection> findReviewStatsByProductIds(Collection<Long> productIds) {
        return qf.select(new QReviewStatsProjection(review.productId, review.rating.avg(), review.count()))
                .from(review)
                .where(review.productId.in(productIds))
                .groupBy(review.productId)
                .fetch();
    }


}
