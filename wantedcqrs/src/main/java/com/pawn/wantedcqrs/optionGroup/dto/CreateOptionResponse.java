package com.pawn.wantedcqrs.optionGroup.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CreateOptionResponse {

    private final Long id;
    private final Long optionGroupId;
    private final String name;
    private final BigDecimal additionalPrice;
    private final String sku;
    private final Integer stock;
    private final Integer displayOrder;

}
