package com.pawn.wantedcqrs.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductImageResponse {

    private Long id;
    private final String url;
    private final String altText;
    private final Boolean isPrimary;
    private final Integer displayOrder;
    private final Long optionId;

}
