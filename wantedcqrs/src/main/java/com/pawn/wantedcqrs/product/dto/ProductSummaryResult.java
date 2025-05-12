package com.pawn.wantedcqrs.product.dto;

import com.pawn.wantedcqrs.product.entity.Currency;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ProductSummaryResult {

    private final Long id;

    private final String name;

    private final String slug;

    private final String shortDescription;

    private final BigDecimal basePrice;

    private final BigDecimal salePrice;

    private final Currency currency;

    private final PrimaryImage primaryImage;

    private final Brand brand;

    private final Seller seller;

    private final double rating;

    private final int reviewCount;

    private final boolean inStock;

    private final ProductStatus status;

    private final LocalDateTime createdAt;


    @Builder
    protected ProductSummaryResult(Long id, String name, String slug, String shortDescription,
                                   BigDecimal basePrice, BigDecimal salePrice, Currency currency,
                                   PrimaryImage primaryImage, Brand brand, Seller seller,
                                   double rating, int reviewCount, boolean inStock,
                                   ProductStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.shortDescription = shortDescription;
        this.basePrice = basePrice;
        this.salePrice = salePrice;
        this.currency = currency;
        this.primaryImage = primaryImage;
        this.brand = brand;
        this.seller = seller;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.inStock = inStock;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Getter
    public static class PrimaryImage {

        private final String url;

        private final String altText;

        @Builder
        protected PrimaryImage(String url, String altText) {
            this.url = url;
            this.altText = altText;
        }
    }

    @Getter
    public static class Brand {

        private final Long id;

        private final String name;

        @Builder
        protected Brand(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Getter
    public static class Seller {

        private final Long id;

        private final String name;

        @Builder
        protected Seller(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}