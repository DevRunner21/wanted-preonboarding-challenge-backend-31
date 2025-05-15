package com.pawn.wantedcqrs.product.dto;

import com.pawn.wantedcqrs.product.entity.Currency;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ProductDetailResponse {

    private Long id;
    private String name;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private SellerDetail seller;
    private BrandDetail brand;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProductDetail detail;
    private PriceDetail price;
    private List<CategoryDetail> categories;
    private List<OptionGroupDetail> optionGroups;
    private List<ImageDetail> images;
    private List<TagDetail> tags;
    private RatingDetail rating;
    private List<RelatedProductDetail> relatedProducts;

    @Getter
    @Builder
    public static class SellerDetail {
        private Long id;
        private String name;
        private String description;
        private String logoUrl;
        private BigDecimal rating;
        private String contactEmail;
        private String contactPhone;
    }

    @Getter
    @Builder
    public static class BrandDetail {
        private Long id;
        private String name;
        private String description;
        private String logoUrl;
        private String website;
    }

    @Getter
    @Builder
    public static class ProductDetail {
        private BigDecimal weight;
        private Dimension dimensions;
        private String materials;
        private String countryOfOrigin;
        private String warrantyInfo;
        private String careInstructions;
        private Map<String, Object> additionalInfo; // JSON object for additional information

        @Getter
        @Builder
        public static class Dimension {
            private Integer width;
            private Integer height;
            private Integer depth;
        }
    }

    @Getter
    @Builder
    public static class PriceDetail {
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private Currency currency;
        private BigDecimal taxRate;
        private Integer discountPercentage;
    }

    @Getter
    @Builder
    public static class CategoryDetail {
        private Long id;
        private String name;
        private String slug;
        private Boolean isPrimary;
        private ParentCategory parent;

        @Getter
        @Builder
        public static class ParentCategory {
            private Long id;
            private String name;
            private String slug;
        }
    }

    @Getter
    @Builder
    public static class OptionGroupDetail {
        private Long id;
        private String name;
        private Integer displayOrder;
        private List<OptionDetail> options;

        @Getter
        @Builder
        public static class OptionDetail {
            private Long id;
            private String name;
            private BigDecimal additionalPrice;
            private String sku;
            private Integer stock;
            private Integer displayOrder;
        }
    }

    @Getter
    @Builder
    public static class ImageDetail {
        private Long id;
        private String url;
        private String altText;
        private Boolean isPrimary;
        private Integer displayOrder;
        private Long optionId;
    }

    @Getter
    @Builder
    public static class TagDetail {
        private Long id;
        private String name;
        private String slug;
    }

    @Getter
    @Builder
    public static class RatingDetail {
        private Double average;
        private Integer count;
        private Map<Integer, Integer> distribution;
    }

    @Getter
    @Builder
    public static class RelatedProductDetail {
        private Long id;
        private String name;
        private String slug;
        private String shortDescription;
        private PrimaryImage primaryImage;
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private Currency currency;

        @Getter
        @Builder
        public static class PrimaryImage {
            private String url;
            private String altText;
        }
    }
}
