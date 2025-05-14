package com.pawn.wantedcqrs.product.service;

import com.pawn.wantedcqrs.brand.dto.BrandDto;
import com.pawn.wantedcqrs.category.dto.CategoryDto;
import com.pawn.wantedcqrs.product.dto.*;
import com.pawn.wantedcqrs.product.repository.dto.ProductSummaryProjection;
import com.pawn.wantedcqrs.productOptionGroup.dto.ProductOptionGroupDto;
import com.pawn.wantedcqrs.productOptionGroup.dto.ProductStockProjection;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;
import com.pawn.wantedcqrs.seller.dto.SellerDto;
import com.pawn.wantedcqrs.tag.dto.TagDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static ProductDetailResponse assemble(
            ProductDto product,
            SellerDto seller,
            BrandDto brand,
            List<ProductOptionGroupDto> optionGroups,
            List<CategoryDto> categories,
            List<ProductImageDto> images,
            List<TagDto> tags,
            ReviewDistributionProjection review
    ) {

        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .shortDescription(product.getShortDescription())
                .fullDescription(product.getFullDescription())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .seller(assembleSeller(seller))
                .brand(assembleBrand(brand))
                .detail(assembleDetail(product.getDetail()))
                .price(assemblePrice(product.getPrice()))
                .categories(assembleCategories(categories, product.getCategoryIds()))
                .optionGroups(assembleOptionGroups(optionGroups))
                .images(assembleImages(images))
                .tags(assembleTags(tags))
                .rating(assembleRating(review))
                .relatedProducts(Collections.emptyList()) // TODO: 관련 상품 구현
                .build();
    }

    private static ProductDetailResponse.SellerDetail assembleSeller(SellerDto dto) {
        return ProductDetailResponse.SellerDetail.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .logoUrl(dto.getLogoUrl())
                .rating(dto.getRating())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .build();
    }

    private static ProductDetailResponse.BrandDetail assembleBrand(BrandDto dto) {
        return ProductDetailResponse.BrandDetail.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .logoUrl(dto.getLogoUrl())
                .website(dto.getWebsite())
                .build();
    }

    private static ProductDetailResponse.ProductDetail assembleDetail(ProductDetailDto dto) {
        return ProductDetailResponse.ProductDetail.builder()
                .weight(dto.getWeight())
                .dimensions(ProductDetailResponse.ProductDetail.Dimension.builder()
                        .width(dto.getDimensions().getWidth())
                        .height(dto.getDimensions().getHeight())
                        .depth(dto.getDimensions().getDepth())
                        .build())
                .materials(dto.getMaterials())
                .countryOfOrigin(dto.getCountryOfOrigin())
                .warrantyInfo(dto.getWarrantyInfo())
                .careInstructions(dto.getCareInstructions())
                .additionalInfo(ProductDetailResponse.ProductDetail.AdditionalInfo.builder()
                        .assemblyRequired(dto.getAdditionalInfo().isAssemblyRequired())
                        .assemblyTime(dto.getAdditionalInfo().getAssemblyTime())
                        .build())
                .build();
    }

    private static ProductDetailResponse.PriceDetail assemblePrice(ProductPriceDto dto) {
        return ProductDetailResponse.PriceDetail.builder()
                .basePrice(dto.getBasePrice())
                .salePrice(dto.getSalePrice())
                .currency(dto.getCurrency())
                .taxRate(dto.getTaxRate())
                .discountPercentage(dto.getDiscountPercentage())
                .build();
    }

    private static List<ProductDetailResponse.CategoryDetail> assembleCategories(List<CategoryDto> categories, List<Long> primaryCategoryIds) {
        return categories.stream()
                .map(c -> ProductDetailResponse.CategoryDetail.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .slug(c.getSlug())
                        .isPrimary(primaryCategoryIds.contains(c.getId()))
                        .parent(c.getParent() != null ? ProductDetailResponse.CategoryDetail.ParentCategory.builder()
                                .id(c.getParent().getId())
                                .name(c.getParent().getName())
                                .slug(c.getParent().getSlug())
                                .build() : null)
                        .build())
                .collect(Collectors.toList());
    }

    private static List<ProductDetailResponse.OptionGroupDetail> assembleOptionGroups(List<ProductOptionGroupDto> groups) {
        return groups.stream()
                .map(g -> ProductDetailResponse.OptionGroupDetail.builder()
                        .id(g.getId())
                        .name(g.getName())
                        .displayOrder(g.getDisplayOrder())
                        .options(g.getOptions().stream()
                                .map(o -> ProductDetailResponse.OptionGroupDetail.OptionDetail.builder()
                                        .id(o.getId())
                                        .name(o.getName())
                                        .additionalPrice(o.getAdditionalPrice())
                                        .sku(o.getSku())
                                        .stock(o.getStock())
                                        .displayOrder(o.getDisplayOrder())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private static List<ProductDetailResponse.ImageDetail> assembleImages(List<ProductImageDto> images) {
        return images.stream()
                .map(i -> ProductDetailResponse.ImageDetail.builder()
                        .id(i.getId())
                        .url(i.getUrl())
                        .altText(i.getAltText())
                        .isPrimary(i.isPrimary())
                        .displayOrder(i.getDisplayOrder())
                        .optionId(i.getOptionId())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<ProductDetailResponse.TagDetail> assembleTags(List<TagDto> tags) {
        return tags.stream()
                .map(t -> ProductDetailResponse.TagDetail.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .slug(t.getSlug())
                        .build())
                .collect(Collectors.toList());
    }

    private static ProductDetailResponse.RatingDetail assembleRating(ReviewDistributionProjection r) {
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(5, r.five().intValue());
        distribution.put(4, r.four().intValue());
        distribution.put(3, r.three().intValue());
        distribution.put(2, r.two().intValue());
        distribution.put(1, r.one().intValue());

        return ProductDetailResponse.RatingDetail.builder()
                .average(r.getAverage())
                .count(r.getTotal().intValue())
                .distribution(distribution)
                .build();
    }

}
