package com.pawn.wantedcqrs.product.service;

import com.pawn.wantedcqrs.product.dto.*;
import com.pawn.wantedcqrs.product.repository.dto.ProductSummaryProjection;
import com.pawn.wantedcqrs.productOptionGroup.dto.ProductOptionGroupDto;
import com.pawn.wantedcqrs.productOptionGroup.dto.ProductStockProjection;
import com.pawn.wantedcqrs.productOptionGroup.service.ProductOptionGroupService;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;
    private final ProductOptionGroupService productOptionGroupService;
    private final ReviewService reviewService;

    private final ProductAssembler productAssembler;

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {

        // TODO: Validation Check
        // Brand Validation Check
        // Seller Validation Check
        // Category Validation Check
        // Tag Validation Check

        // Product 등록
        ProductDto savedProductDto = productService.create(request.toProductDto());

        // ProductOptionGroup 저장
        List<ProductOptionGroupDto> productOptionGroups =
                productOptionGroupService.createProductOptionGroupsByProductId(
                        savedProductDto.getId(), request.toOptionGroupDtos()
                );

        // ProductCategory
        List<ProductCategoryDto> productCategoryDtos = request.toProductCategoryDtos(savedProductDto.getId());
        productService.saveCategoriesByProductId(savedProductDto.getId(), productCategoryDtos);

        // ProductTag
        List<ProductTagDto> productTagDtos = request.toProductTagDtos(savedProductDto.getId());
        productService.saveTagsByProductId(savedProductDto.getId(), productTagDtos);

        // ProductImage
        List<ProductImageDto> productImageDtos = request.toProductImageDtos(savedProductDto.getId());
        productService.saveImagesByProductId(savedProductDto.getId(), productImageDtos);


        return CreateProductResponse.builder()
                .id(savedProductDto.getId())
                .name(savedProductDto.getName())
                .slug(savedProductDto.getSlug())
                .createdAt(savedProductDto.getCreatedAt())
                .updatedAt(savedProductDto.getUpdatedAt())
                .build();
    }

    public Page<ProductSummaryResult> getProductSummaryPages(ProductQueryCondition condition, Pageable pageable) {

        Page<ProductSummaryProjection> productSummaryPage = productService.getProductSummaryPage(condition, pageable);
        List<Long> productIds = productSummaryPage.getContent().stream().map(ProductSummaryProjection::getId).toList();

        if (productIds.isEmpty()) {
            return Page.empty();
        }

        // primaryImage
        Map<Long, ProductImageDto> primaryProductImageMap = productService.getPrimaryProductImageMapBy(productIds);

        // review
        Map<Long, ReviewStatsProjection> reviewStatsMap = reviewService.getReviewStatsMapBy(productIds);

        // stock
        Map<Long, ProductStockProjection> productStockMap = productOptionGroupService.getProductStockMapBy(productIds);

        List<ProductSummaryResult> results = productAssembler.assemble(
                productSummaryPage.getContent(),
                primaryProductImageMap,
                reviewStatsMap,
                productStockMap
        );

        return new PageImpl<>(results, pageable, productSummaryPage.getTotalElements());
    }

}
