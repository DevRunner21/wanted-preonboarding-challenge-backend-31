package com.pawn.wantedcqrs.review.repository;

import com.pawn.wantedcqrs.review.dto.QReviewStatsProjection;
import com.pawn.wantedcqrs.review.dto.ReviewQueryCondition;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.entity.Review;
import com.pawn.wantedcqrs.review.repository.dto.QReviewDistributionProjection;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.pawn.wantedcqrs.review.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class QReviewRepositoryCustomImpl implements QReviewRepositoryCustom {

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

    @Override
    public Page<Review> findReviewPageBy(ReviewQueryCondition condition, Pageable pageable) {
        Long total = getTotalCountQuery(condition);

        if (total == null || total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<Review> contents = getContentQuery(condition, pageable).fetch();

        return new PageImpl<>(contents, pageable, total);
    }

    private Long getTotalCountQuery(ReviewQueryCondition condition) {
        JPAQuery<Long> countQuery = qf.select(review.count())
                .from(review);

        applyReviewQueryCondition(countQuery, condition);

        return countQuery.fetchOne();
    }

    private void applyReviewQueryCondition(JPAQuery<?> query, ReviewQueryCondition condition) {
        // 상품 별 조회 조건
        if (condition.getProductId() != null) {
            query.where(review.productId.eq(condition.getProductId()));
        }

        // 평점 조회 조건
        if (condition.getRating() != null) {
            query.where(review.rating.eq(condition.getRating()));
        }

    }

    private JPAQuery<Review> getContentQuery(ReviewQueryCondition condition, Pageable pageable) {
        JPAQuery<Review> contentQuery = qf.select(review)
                .from(review);

        // 필터 조건 적용
        applyReviewQueryCondition(contentQuery, condition);

        // 정렬
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());
        contentQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));

        return contentQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
        PathBuilder<Review> entityPath = new PathBuilder<>(Review.class, review.getMetadata());

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            if ("id".equals(property)) {
                orders.add(new OrderSpecifier<>(direction, entityPath.getNumber(property, Long.class)));
            } else if ("created_at".equals(property)) {
                orders.add(new OrderSpecifier<>(direction, entityPath.getDateTime("createdAt", LocalDateTime.class)));
            } else if ("product_id".equals(property)) {
                orders.add(new OrderSpecifier<>(direction, entityPath.getNumber(property, Long.class)));
            } else {
                orders.add(new OrderSpecifier<>(direction, entityPath.getString(property)));
            }
        }

        return orders;
    }

}
