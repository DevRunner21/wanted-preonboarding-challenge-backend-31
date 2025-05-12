package com.pawn.wantedcqrs.product.service;

import com.pawn.wantedcqrs.common.exception.e4xx.ConflictException;
import com.pawn.wantedcqrs.product.dto.*;
import com.pawn.wantedcqrs.product.entity.Product;
import com.pawn.wantedcqrs.product.entity.ProductCategory;
import com.pawn.wantedcqrs.product.entity.ProductImage;
import com.pawn.wantedcqrs.product.entity.ProductTag;
import com.pawn.wantedcqrs.product.repository.ProductCategoryRepository;
import com.pawn.wantedcqrs.product.repository.ProductImageRepository;
import com.pawn.wantedcqrs.product.repository.ProductRepository;
import com.pawn.wantedcqrs.product.repository.ProductTagRepository;
import com.pawn.wantedcqrs.product.repository.dto.ProductSummaryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductTagRepository productTagRepository;

    private final ProductImageRepository productImageRepository;

    @Transactional
    public ProductDto create(ProductDto productParam) {

        // Product 저장
        checkDupSlug(productParam.getSlug());

        ProductDetailDto detailParam = productParam.getDetail();
        ProductPriceDto priceParam = productParam.getPrice();

        Product newProduct = productParam.toEntity(
                detailParam,
                priceParam
        );

        Product savedProduct = productRepository.save(newProduct);

        return ProductDto.fromEntity(savedProduct);
    }

    private void checkDupSlug(String slug) {
        boolean isDupProduct = productRepository.findBySlug(slug).isPresent();
        if (isDupProduct) {
            throw ConflictException.PRODUCT_SLUG_DUP.getResponseException();
        }
    }

    @Transactional
    public void saveCategoriesByProductId(Long productId, List<ProductCategoryDto> productCategoryDtos) {
//        List<ProductCategory> productCategories = productCategoryDtos.stream().map(ProductCategoryDto::toEntity).toList();
        List<ProductCategory> productCategories = productCategoryDtos.stream()
                .map(dto -> ProductCategory.builder()
                        .productId(productId) // 전달된 productId 사용
                        .categoryId(dto.getCategoryId())
                        .isPrimary(dto.isPrimary())
                        .build())
                .toList();

        productCategoryRepository.saveAll(productCategories);
    }

    @Transactional
    public void saveTagsByProductId(Long productId, List<ProductTagDto> productTagDtos) {
//        List<ProductTag> productTags = productTagDtos.stream().map(ProductTagDto::toEntity).toList();
        List<ProductTag> productTags = productTagDtos.stream()
                .map(dto -> ProductTag.builder()
                        .productId(productId) // 전달된 productId 사용
                        .tagId(dto.getTagId())
                        .build())
                .toList();

        productTagRepository.saveAll(productTags);
    }

    @Transactional
    public void saveImagesByProductId(Long productId, List<ProductImageDto> productImageDtos) {
        List<ProductImage> productImages = productImageDtos.stream()
                .map(dto -> ProductImage.builder()
                        .productId(productId)
                        .isPrimary(dto.isPrimary())
                        .url(dto.getUrl())
                        .altText(dto.getAltText())
                        .displayOrder(dto.getDisplayOrder())
                        .optionId(dto.getOptionId())
                        .build()).toList();

        productImageRepository.saveAll(productImages);
    }

    @Transactional(readOnly = true)
    public Page<ProductSummaryProjection> getProductSummaryPage(ProductQueryCondition condition, Pageable pageable) {

        return productRepository.findProductSummaryPage(condition, pageable);
    }

    @Transactional(readOnly = true)
    public Map<Long, ProductImageDto> getPrimaryProductImageMapBy(List<Long> productIds) {
        List<ProductImage> primaryProductImages = productImageRepository.findAllByProductIdsAndIsPrimary(productIds);

        return primaryProductImages.stream()
                .map(ProductImageDto::fromEntity)
                .collect(Collectors.toMap(
                        ProductImageDto::getProductId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

}
