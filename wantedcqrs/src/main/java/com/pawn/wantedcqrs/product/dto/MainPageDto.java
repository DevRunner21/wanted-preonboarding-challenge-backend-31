package com.pawn.wantedcqrs.product.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MainPageDto {

    private List<ProductSummaryResult> newProducts = new ArrayList<>();
    private List<ProductSummaryResult> popularProducts = new ArrayList<>();;
    private List<FeaturedCategory> featuredCategories = new ArrayList<>();;

    @Data
    @Builder
    public static class FeaturedCategory {
        private Long id;
        private String name;
        private String slug;
        private String imageUrl;
        private Integer productCount;
    }

}