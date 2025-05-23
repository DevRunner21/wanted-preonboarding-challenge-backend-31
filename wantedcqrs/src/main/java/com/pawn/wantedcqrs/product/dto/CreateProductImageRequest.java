package com.pawn.wantedcqrs.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductImageRequest {

    private final String url;
    private final String altText;
    private final Boolean isPrimary;
    private final Integer displayOrder;
    private final Long optionId;

    public ProductImageDto toProductImageDto() {
        ProductImageDto dto = new ProductImageDto();
        dto.setUrl(url);
        dto.setAltText(altText);
        dto.setPrimary(isPrimary);
        dto.setDisplayOrder(displayOrder);
        dto.setOptionId(optionId);

        return dto;
    }

}
