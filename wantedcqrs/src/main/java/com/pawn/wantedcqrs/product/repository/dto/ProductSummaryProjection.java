package com.pawn.wantedcqrs.product.repository.dto;

import com.pawn.wantedcqrs.product.entity.Currency;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ProductSummaryProjection {
    private final Long id;
    private final String name;
    private final String slug;
    private final String shortDescription;
    private final BigDecimal basePrice;
    private final BigDecimal salePrice;
    private final Currency currency;
    private final Long brandId;
    private final String brandName;
    private final Long sellerId;
    private final String sellerName;
    private final ProductStatus status;
    private final LocalDateTime createdAt;

    @QueryProjection
    public ProductSummaryProjection(Long id, String name, String slug, String shortDescription, BigDecimal basePrice, BigDecimal salePrice, Currency currency, Long brandId, String brandName, Long sellerId, String sellerName, ProductStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.shortDescription = shortDescription;
        this.basePrice = basePrice;
        this.salePrice = salePrice;
        this.currency = currency;
        this.brandId = brandId;
        this.brandName = brandName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.status = status;
        this.createdAt = createdAt;
    }

}
