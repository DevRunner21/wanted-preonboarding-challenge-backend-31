package com.pawn.wantedcqrs.optionGroup.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UpdateOptionRequest {

    private final Long optionGroupId;
    private final String name;
    private final BigDecimal additionalPrice;
    private final String sku;
    private final Integer stock;
    private final Integer displayOrder;

    public ProductOptionDto toProductOptionDto() {
        ProductOptionDto dto = new ProductOptionDto();
        dto.setOptionGroupId(this.optionGroupId);
        dto.setName(this.name);
        dto.setAdditionalPrice(this.additionalPrice);
        dto.setSku(this.sku);
        dto.setStock(this.stock);
        dto.setDisplayOrder(this.displayOrder);

        return dto;
    }

}
