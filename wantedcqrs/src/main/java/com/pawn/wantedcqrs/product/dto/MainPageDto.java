package com.pawn.wantedcqrs.product.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MainPageDto {

    @Builder.Default
    private List<ProductSummaryResult> newProducts = new ArrayList<>();
    @Builder.Default
    private List<ProductSummaryResult> popularProducts = new ArrayList<>();;
    @Builder.Default
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