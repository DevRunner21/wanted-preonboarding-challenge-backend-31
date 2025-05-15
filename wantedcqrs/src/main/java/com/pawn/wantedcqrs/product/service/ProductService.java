package com.pawn.wantedcqrs.product.service;

import com.pawn.wantedcqrs.common.exception.e4xx.ConflictException;
import com.pawn.wantedcqrs.common.exception.e4xx.ResourceNotFoundException;
import com.pawn.wantedcqrs.product.dto.*;
import com.pawn.wantedcqrs.product.entity.Product;
import com.pawn.wantedcqrs.product.entity.ProductCategory;
import com.pawn.wantedcqrs.product.entity.ProductImage;
import com.pawn.wantedcqrs.product.entity.ProductTag;
import com.pawn.wantedcqrs.product.repository.ProductImageRepository;
import com.pawn.wantedcqrs.product.repository.ProductRepository;
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

    private final ProductImageRepository productImageRepository;

    @Transactional
    public ProductDto create(ProductDto productParam
            , List<ProductCategoryDto> productCategoryDtos
            , List<ProductTagDto> productTagDtos
            , List<ProductImageDto> productImageDtos
    ) {

        // Product 저장
        checkDupSlug(productParam.getSlug());

        ProductDetailDto detailParam = productParam.getDetail();
        ProductPriceDto priceParam = productParam.getPrice();

        Product newProduct = productParam.toEntity(
                detailParam,
                priceParam
        );

        Product savedProduct = productRepository.save(newProduct);

        // 카테고리 추가
        addCategories(productCategoryDtos, savedProduct);

        // 태그 추가
        addTags(productTagDtos, savedProduct);

        // 이미지 추가
        addImages(productImageDtos, savedProduct);

        return ProductDto.fromEntity(savedProduct);
    }


    private void addCategories(List<ProductCategoryDto> productCategoryDtos, Product savedProduct) {
        List<ProductCategory> productCategories = productCategoryDtos.stream().map(dto -> ProductCategory.builder()
                .product(savedProduct)
                .categoryId(dto.getCategoryId())
                .isPrimary(dto.isPrimary())
                .build()).toList();
        productCategories.forEach(savedProduct::addCategory);
    }

    private void addTags(List<ProductTagDto> productTagDtos, Product savedProduct) {
        List<ProductTag> tags = productTagDtos.stream().map(dto -> ProductTag.builder()
                .product(savedProduct) // 전달된 productId 사용
                .tagId(dto.getTagId())
                .build()).toList();
        tags.forEach(savedProduct::addTag);
    }

    private void addImages(List<ProductImageDto> productImageDtos, Product savedProduct) {
        List<ProductImage> productImages = productImageDtos.stream().map(dto -> ProductImage.builder()
                .product(savedProduct)
                .isPrimary(dto.isPrimary())
                .url(dto.getUrl())
                .altText(dto.getAltText())
                .displayOrder(dto.getDisplayOrder())
                .optionId(dto.getOptionId())
                .build()).toList();
        productImages.forEach(savedProduct::addImage);
    }

    private void checkDupSlug(String slug) {
        boolean isDupProduct = productRepository.findBySlug(slug).isPresent();
        if (isDupProduct) {
            throw ConflictException.PRODUCT_SLUG_DUP.getResponseException();
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductSummaryProjection> getProductSummaryPage(ProductQueryCondition condition, Pageable pageable) {

        return productRepository.findProductSummaryPage(condition, pageable);
    }

    @Transactional(readOnly = true)
    public List<ProductSummaryProjection> getProductSummariesBy(List<Long> productIds) {

        return productRepository.findProductSummariesBy(productIds);
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

    public ProductDto getProductBy(Long productId) {
        Product foundProduct = productRepository.findById(productId).orElseThrow(ResourceNotFoundException.PRODUCT::getResponseException);

        return ProductDto.fromEntity(foundProduct);
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto, List<ProductCategoryDto> productCategoryDtos, List<ProductTagDto> productTagDtos) {
        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(ResourceNotFoundException.PRODUCT::getResponseException);

        foundProduct.updateProduct(productDto);
        foundProduct.getProductDetail().updateProductDetail(productDto.getDetail());
        foundProduct.getProductPrice().updateProductPrice(productDto.getPrice());

        // 카테고리 수정
        List<ProductCategory> newProductCategories = productCategoryDtos.stream().map(productCategoryDto -> ProductCategory.builder()
                .product(foundProduct)
                .id(productCategoryDto.getId())
                .categoryId(productCategoryDto.getCategoryId())
                .isPrimary(productCategoryDto.isPrimary())
                .build()).toList();
        foundProduct.getCategories().clear();
        foundProduct.getCategories().addAll(newProductCategories);

        // 태그 수정
        List<ProductTag> newProductTags = productTagDtos.stream().map(productTagDto -> ProductTag.builder()
                .id(productTagDto.getId())
                .product(foundProduct)
                .tagId(productTagDto.getTagId())
                .build()).toList();
        foundProduct.getTags().clear();
        foundProduct.getTags().addAll(newProductTags);

        return ProductDto.fromEntity(foundProduct);
    }


}
