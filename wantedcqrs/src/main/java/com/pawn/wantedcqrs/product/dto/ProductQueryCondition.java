package com.pawn.wantedcqrs.product.dto;

import com.pawn.wantedcqrs.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Getter
public class ProductQueryCondition {

    private final ProductStatus status;

    private final Integer minPrice;

    private final Integer maxPrice;

    private final Collection<Long> categoryIds;

    private final Long sellerId;

    private final Long brandId;

    private final Boolean inStock;

    private final String search;

    @Builder
    protected ProductQueryCondition(ProductStatus status, Integer minPrice, Integer maxPrice, Collection<Long> categoryIds, Long sellerId, Long brandId, Boolean inStock, String search) {
        this.status = status;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.categoryIds = categoryIds;
        this.sellerId = sellerId;
        this.brandId = brandId;
        this.inStock = inStock;
        this.search = search;
    }

}
