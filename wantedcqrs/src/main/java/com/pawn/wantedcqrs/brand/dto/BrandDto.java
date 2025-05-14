package com.pawn.wantedcqrs.brand.dto;

import com.pawn.wantedcqrs.brand.entity.Brand;
import lombok.Data;

@Data
public class BrandDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private String website;

    public static BrandDto fromEntity(Brand brand) {
        if (brand == null) return null;

        BrandDto dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setSlug(brand.getSlug());
        dto.setDescription(brand.getDescription());
        dto.setLogoUrl(brand.getLogoUrl());
        dto.setWebsite(brand.getWebsite());
        return dto;
    }
}
