package com.pawn.wantedcqrs.optionGroup.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProductStockProjection {
    private final Long productId;
    private final int stockCount;

    @QueryProjection
    public ProductStockProjection(Long productId, int stockCount) {
        this.productId = productId;
        this.stockCount = stockCount;
    }

}
