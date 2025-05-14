package com.pawn.wantedcqrs.review.repository;

import com.pawn.wantedcqrs.review.dto.QReviewStatsProjection;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.repository.dto.QReviewDistributionProjection;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
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

    @Override
    public ReviewDistributionProjection findDistributionBy(Long productId) {
        NumberExpression<Long> fiveStar = new CaseBuilder()
                .when(review.rating.eq(5)).then(1L)
                .otherwise(0L);
        NumberExpression<Long> fourStar = new CaseBuilder()
                .when(review.rating.eq(4)).then(1L)
                .otherwise(0L);
        NumberExpression<Long> threeStar = new CaseBuilder()
                .when(review.rating.eq(3)).then(1L)
                .otherwise(0L);
        NumberExpression<Long> twoStar = new CaseBuilder()
                .when(review.rating.eq(2)).then(1L)
                .otherwise(0L);
        NumberExpression<Long> oneStar = new CaseBuilder()
                .when(review.rating.eq(1)).then(1L)
                .otherwise(0L);

        return qf.select(new QReviewDistributionProjection(
                        fiveStar.sum(),
                        fourStar.sum(),
                        threeStar.sum(),
                        twoStar.sum(),
                        oneStar.sum()
                ))
                .from(review)
                .where(review.productId.eq(productId))
                .fetchOne();
    }

}
