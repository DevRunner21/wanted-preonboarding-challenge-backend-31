package com.pawn.wantedcqrs.product.dto;

import com.pawn.wantedcqrs.product.entity.Currency;
import com.pawn.wantedcqrs.product.entity.ProductPrice;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class ProductPriceDto {

    private Long id;

    private Long productId;

    private BigDecimal basePrice;

    private BigDecimal salePrice;

    private BigDecimal costPrice;

    private Currency currency;

    private BigDecimal taxRate;

    public ProductPrice toEntity() {
        return ProductPrice.builder()
                .id(id)
//                .product(productId)
                .basePrice(basePrice)
                .salePrice(salePrice)
                .costPrice(costPrice)
                .currency(currency)
                .taxRate(taxRate)
                .build();
    }

    public Integer getDiscountPercentage() {
        return basePrice.subtract(salePrice)
                .divide(basePrice, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP)
                .intValue();
    }

}
