package com.pawn.wantedcqrs.product.dto;

import com.pawn.wantedcqrs.product.entity.ProductDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class ProductDetailDto {

    private Long id;

    private Long productId;

    private BigDecimal weight;

    private ProductDetailDto.Dimensions dimensions;

    private String materials;

    private String countryOfOrigin;

    private String warrantyInfo;

    private String careInstructions;

    private Map<String, Object> additionalInfo = new HashMap<>(); // JSON object for additional information

    @Data
    public static class Dimensions {

        private int width;

        private int height;

        private int depth;

        public Dimensions(int width, int height, int depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

    }

    public ProductDetail toEntity() {
        return ProductDetail.builder()
                .id(id)
//                .product(product)
                .weight(weight)
                .dimensions(ProductDetail.Dimensions.builder()
                        .depth(dimensions.depth)
                        .width(dimensions.width)
                        .height(dimensions.height)
                        .build()
                ).materials(materials)
                .countryOfOrigin(countryOfOrigin)
                .warrantyInfo(warrantyInfo)
                .careInstructions(careInstructions)
                .additionalInfo(additionalInfo)
                .build();
    }

}
