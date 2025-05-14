package com.pawn.wantedcqrs.product.repository;

import com.pawn.wantedcqrs.product.dto.ProductQueryCondition;
import com.pawn.wantedcqrs.product.entity.Product;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import com.pawn.wantedcqrs.product.repository.dto.ProductSummaryProjection;
import com.pawn.wantedcqrs.product.repository.dto.QProductSummaryProjection;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.pawn.wantedcqrs.brand.entity.QBrand.brand;
import static com.pawn.wantedcqrs.product.entity.QProduct.product;
import static com.pawn.wantedcqrs.product.entity.QProductCategory.productCategory;
import static com.pawn.wantedcqrs.product.entity.QProductDetail.productDetail;
import static com.pawn.wantedcqrs.product.entity.QProductPrice.productPrice;
import static com.pawn.wantedcqrs.productOptionGroup.entity.QProductOption.productOption;
import static com.pawn.wantedcqrs.seller.entity.QSeller.seller;
import static java.util.Objects.nonNull;

@Repository
@RequiredArgsConstructor
public class QProductRepositoryCustomImpl implements QProductRepositoryCustom {

    private final JPAQueryFactory qf;

    public Page<ProductSummaryProjection> findProductSummaryPage(ProductQueryCondition productQueryCondition, Pageable pageable) {
        Long total = getTotalCountQuery(productQueryCondition);

        if (total == null || total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<ProductSummaryProjection> contents = getContentQuery(productQueryCondition, pageable).fetch();

        return new PageImpl<>(contents, pageable, total);
    }

    private JPAQuery<ProductSummaryProjection> getContentQuery(ProductQueryCondition condition, Pageable pageable) {
        JPAQuery<ProductSummaryProjection> contentQuery = qf.select(new QProductSummaryProjection(
                        product.id,
                        product.name,
                        product.slug,
                        product.shortDescription,
                        product.productPrice.basePrice,
                        product.productPrice.salePrice,
                        product.productPrice.currency,
                        brand.id,
                        brand.name,
                        seller.id,
                        seller.name,
                        product.status,
                        product.createdAt
                ))
                .from(product)
                .leftJoin(product.productDetail, productDetail)
                .leftJoin(product.productPrice, productPrice)
                .leftJoin(brand).on(product.brandId.eq(brand.id))
                .leftJoin(seller).on(product.sellerId.eq(seller.id));

        // 필터조건 적용
        applyProductQueryCondition(contentQuery, condition);

        // 정렬
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());
        contentQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));

        return contentQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private Long getTotalCountQuery(ProductQueryCondition condition) {
        JPAQuery<Long> countQuery = qf.select(product.count())
                .from(product);

        applyProductQueryCondition(countQuery, condition);

        return countQuery.fetchOne();
    }

    private static void applyProductQueryCondition(JPAQuery<?> query, ProductQueryCondition condition) {
        // categoryIds 조건 체크
        if (!CollectionUtils.isEmpty(condition.getCategoryIds())) {
            query.join(productCategory).on(productCategory.product.id.eq(product.id)).fetchJoin()
                    .where(productCategory.categoryId.in(condition.getCategoryIds()));
        }

        // sellerId 조건 체크
        if (condition.getSellerId() != null) {
            query.where(product.sellerId.eq(condition.getSellerId()));
        }

        // brandId 조건 체크
        if (condition.getSellerId() != null) {
            query.where(product.brandId.eq(condition.getBrandId()));
        }

        // ProductStatus 조건 체크
        if (condition.getStatus() != null) {
            query.where(product.status.eq(condition.getStatus()));
        } else {
            query.where(product.status.ne(ProductStatus.DELETED));
        }

        // minPrice
        if (condition.getMinPrice() != null && condition.getMinPrice() > 0) {
            query.where(product.productPrice.basePrice.goe(condition.getMinPrice()));
        }

        // maxPrice
        if (condition.getMaxPrice() != null && condition.getMaxPrice() > 0) {
            query.where(product.productPrice.basePrice.loe(condition.getMaxPrice()));
        }

        // inStock
        if (nonNull(condition.getInStock())) {
            query.distinct().join(productOption).on(productOption.optionGroup.productId.eq(product.id)).fetchJoin()
                    .where(
                            Boolean.TRUE.equals(condition.getInStock())
                                    ? productOption.stock.gt(0)
                                    : productOption.stock.eq(0)
                    );
        }

        // search
        if (nonNull(condition.getSearch())) {
            query.where(
                    product.name.containsIgnoreCase(condition.getSearch())
                            .or(product.shortDescription.containsIgnoreCase(condition.getSearch()))
                            .or(product.fullDescription.containsIgnoreCase(condition.getSearch())
                            ));
        }

    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
//        PathBuilder<?> entityPath = new PathBuilder<>((Class<?>) Product.class, "productEntity");
        PathBuilder<Product> entityPath = new PathBuilder<>(Product.class, product.getMetadata());

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            if ("price".equals(property)) {
                orders.add(new OrderSpecifier<>(direction, entityPath.getNumber(property, BigDecimal.class)));
            } else if ("created_at".equals(property)) {
                orders.add(new OrderSpecifier<>(direction, entityPath.getDateTime("createdAt", LocalDateTime.class)));
            } else {
                orders.add(new OrderSpecifier<>(direction, entityPath.getString(property)));
            }
        }

        return orders;
    }

}
