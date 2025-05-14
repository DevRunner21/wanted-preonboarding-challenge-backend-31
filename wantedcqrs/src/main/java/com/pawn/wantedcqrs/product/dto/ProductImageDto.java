package com.pawn.wantedcqrs.product.dto;

import com.pawn.wantedcqrs.product.entity.ProductImage;
import lombok.Data;

@Data
public class ProductImageDto {

    private Long id;

    private String url;

    private String altText;

    private boolean isPrimary;

    private int displayOrder;

    private Long productId;

    private Long optionId;

    public static ProductImageDto fromEntity(ProductImage entity) {
        ProductImageDto dto = new ProductImageDto();
        dto.setId(entity.getId());
        dto.setUrl(entity.getUrl());
        dto.setAltText(entity.getAltText());
        dto.setPrimary(entity.getIsPrimary());
        dto.setDisplayOrder(entity.getDisplayOrder());
        dto.setProductId(entity.getProduct().getId());
        dto.setOptionId(entity.getOptionId());
        return dto;
    }

}
