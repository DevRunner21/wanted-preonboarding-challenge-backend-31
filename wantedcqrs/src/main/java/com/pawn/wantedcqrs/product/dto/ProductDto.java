package com.pawn.wantedcqrs.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pawn.wantedcqrs.product.entity.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductDto {

    private Long id;

    private String name;

    private String slug;

    private String shortDescription;

    private String fullDescription;

    private Long sellerId;

    private Long brandId;

    private ProductStatus status;

    private ProductDetailDto detail;

    private ProductPriceDto price;

    private List<Long> categoryIds = new ArrayList<>();

    private List<Long> tagIds = new ArrayList<>();

    private List<ProductImageDto> images = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public Product toEntity(ProductDetailDto detail, ProductPriceDto price) {
        return Product.builder()
                .id(id)
                .name(name)
                .slug(slug)
                .shortDescription(shortDescription)
                .fullDescription(fullDescription)
                .sellerId(sellerId)
                .brandId(brandId)
                .status(status)
                .productDetail(detail.toEntity())
                .productPrice(price.toEntity())
                .build();
    }

    public static ProductDto fromEntity(Product product) {
        if (product == null) return null;

        ProductDetail detail = product.getProductDetail();
        ProductDetailDto detailDto = new ProductDetailDto();
        detailDto.setId(detail.getId());
        detailDto.setWeight(detail.getWeight());
        detailDto.setMaterials(detail.getMaterials());
        detailDto.setCountryOfOrigin(detail.getCountryOfOrigin());
        detailDto.setWarrantyInfo(detail.getWarrantyInfo());
        detailDto.setCareInstructions(detail.getCareInstructions());
        detailDto.setDimensions(new ProductDetailDto.Dimensions(
                detail.getDimensions().getWidth(),
                detail.getDimensions().getHeight(),
                detail.getDimensions().getDepth()
        ));
        detailDto.setAdditionalInfo(detail.getAdditionalInfo());

        ProductPrice price = product.getProductPrice();
        ProductPriceDto priceDto = new ProductPriceDto();
        priceDto.setId(price.getId());
        priceDto.setBasePrice(price.getBasePrice());
        priceDto.setSalePrice(price.getSalePrice());
        priceDto.setCostPrice(price.getCostPrice());
        priceDto.setCurrency(price.getCurrency());
        priceDto.setTaxRate(price.getTaxRate());

        List<ProductImageDto> productImageDtos = product.getImages().stream().map(ProductImageDto::fromEntity).toList();

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setShortDescription(product.getShortDescription());
        dto.setFullDescription(product.getFullDescription());
        dto.setSellerId(product.getSellerId());
        dto.setBrandId(product.getBrandId());
        dto.setStatus(product.getStatus());
        dto.setDetail(detailDto);
        dto.setPrice(priceDto);
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setCategoryIds(product.getCategories().stream().map(ProductCategory::getId).collect(Collectors.toList()));
        dto.setTagIds(product.getTags().stream().map(ProductTag::getId).collect(Collectors.toList()));
        dto.setImages(productImageDtos);

        return dto;
    }

}
