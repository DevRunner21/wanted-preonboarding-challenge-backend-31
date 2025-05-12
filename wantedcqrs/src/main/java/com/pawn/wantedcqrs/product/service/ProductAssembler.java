package com.pawn.wantedcqrs.product.service;

import com.pawn.wantedcqrs.product.dto.ProductImageDto;
import com.pawn.wantedcqrs.product.dto.ProductSummaryResult;
import com.pawn.wantedcqrs.product.repository.dto.ProductSummaryProjection;
import com.pawn.wantedcqrs.productOptionGroup.dto.ProductStockProjection;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductAssembler {

    public List<ProductSummaryResult> assemble(
            List<ProductSummaryProjection> products,
            Map<Long, ProductImageDto> primaryImageMap,
            Map<Long, ReviewStatsProjection> reviewStatsMap,
            Map<Long, ProductStockProjection> stockMap
    ) {
        return products.stream()
                .map(product -> {
                    // 이미지
                    ProductImageDto imageDto = primaryImageMap.get(product.getId());
                    ProductSummaryResult.PrimaryImage primaryImage = imageDto != null
                            ? ProductSummaryResult.PrimaryImage.builder()
                            .url(imageDto.getUrl())
                            .altText(imageDto.getAltText())
                            .build()
                            : null;

                    // 리뷰 통계
                    ReviewStatsProjection reviewStats = reviewStatsMap.get(product.getId());
                    double rating = reviewStats != null ? reviewStats.getRating() : 0.0;
                    int reviewCount = reviewStats != null ? reviewStats.getReviewCount().intValue() : 0;

                    // 재고
                    ProductStockProjection stock = stockMap.get(product.getId());
                    boolean inStock = stock != null && stock.getStockCount() > 0;

                    return ProductSummaryResult.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .slug(product.getSlug())
                            .shortDescription(product.getShortDescription())
                            .basePrice(product.getBasePrice())
                            .salePrice(product.getSalePrice())
                            .currency(product.getCurrency())
                            .primaryImage(primaryImage)
                            .brand(ProductSummaryResult.Brand.builder()
                                    .id(product.getBrandId())
                                    .name(product.getBrandName())
                                    .build())
                            .seller(ProductSummaryResult.Seller.builder()
                                    .id(product.getSellerId())
                                    .name(product.getSellerName())
                                    .build())
                            .rating(rating)
                            .reviewCount(reviewCount)
                            .inStock(inStock)
                            .status(product.getStatus())
                            .createdAt(product.getCreatedAt())
                            .build();
                })
                .toList();
    }
}
