package com.pawn.wantedcqrs.product.service;

import com.pawn.wantedcqrs.brand.dto.BrandDto;
import com.pawn.wantedcqrs.brand.service.BrandService;
import com.pawn.wantedcqrs.category.dto.CategoryDto;
import com.pawn.wantedcqrs.category.service.CategoryService;
import com.pawn.wantedcqrs.product.dto.*;
import com.pawn.wantedcqrs.product.repository.dto.ProductSummaryProjection;
import com.pawn.wantedcqrs.productOptionGroup.dto.ProductOptionGroupDto;
import com.pawn.wantedcqrs.productOptionGroup.dto.ProductStockProjection;
import com.pawn.wantedcqrs.productOptionGroup.service.ProductOptionGroupService;
import com.pawn.wantedcqrs.review.dto.ReviewStatsProjection;
import com.pawn.wantedcqrs.review.repository.dto.ReviewDistributionProjection;
import com.pawn.wantedcqrs.review.service.ReviewService;
import com.pawn.wantedcqrs.seller.dto.SellerDto;
import com.pawn.wantedcqrs.seller.service.SellerService;
import com.pawn.wantedcqrs.tag.dto.TagDto;
import com.pawn.wantedcqrs.tag.service.TagService;
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
    private final SellerService sellerService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final TagService tagService;

    private final ProductAssembler productAssembler;

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {

        // Request 분해
        ProductDto productDto = request.toProductDto();
        List<ProductCategoryDto> productCategoryDtos = request.toProductCategoryDtos();
        List<ProductTagDto> productTagDtos = request.toProductTagDtos();
        List<ProductImageDto> productImageDtos = request.toProductImageDtos();

        // Product, ProductCategory, ProductTag, ProductImage 등록
        // TODO: Validation Check
        // Brand Validation Check
        // Seller Validation Check
        // Category Validation Check
        // Tag Validation Check
        ProductDto savedProductDto = productService.create(productDto, productCategoryDtos, productTagDtos, productImageDtos);

        // ProductOptionGroup 저장
        List<ProductOptionGroupDto> productOptionGroups =
                productOptionGroupService.createProductOptionGroupsByProductId(
                        savedProductDto.getId(), request.toOptionGroupDtos()
                );

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

    public ProductDetailResponse getProductDetail(Long productId) {
        // 상품 조회
        ProductDto product = productService.getProductBy(productId);

        // Seller 조회
        SellerDto seller = sellerService.getSellerBy(product.getSellerId());

        // Brand 조회
        BrandDto brand = brandService.getBrandBy(product.getBrandId());

        // 옵션 그룹, 옵션 조회
        List<ProductOptionGroupDto> optionGroups = productOptionGroupService.getOptionGroupsBy(productId);

        // 카테고리 조회
        List<Long> categoryIds = product.getCategoryIds();
        List<CategoryDto> categories = categoryService.getCategoriesBy(categoryIds);

        // 태그 조회
        List<Long> tagIds = product.getTagIds();
        List<TagDto> tags = tagService.getTagsBy(tagIds);

        // 상품 이미지 조회
        List<ProductImageDto> images = product.getImages();

        // 평점 조회
        ReviewDistributionProjection reviewDistribution = reviewService.findDistributionBy(productId);

        // TODO: 관련 상품 조회

        // 병합
        return productAssembler.assemble(
                product,
                seller,
                brand,
                optionGroups,
                categories,
                images,
                tags,
                reviewDistribution
        );
    }


}
