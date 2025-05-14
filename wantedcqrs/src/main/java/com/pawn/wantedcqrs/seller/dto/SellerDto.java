package com.pawn.wantedcqrs.seller.dto;

import com.pawn.wantedcqrs.seller.entity.Seller;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SellerDto {
    private Long id;
    private String name;
    private String description;
    private String logoUrl;
    private BigDecimal rating;
    private String contactEmail;
    private String contactPhone;
    private LocalDateTime createdAt;

    public static SellerDto fromEntity(Seller seller) {
        if (seller == null) return null;

        SellerDto dto = new SellerDto();
        dto.setId(seller.getId());
        dto.setName(seller.getName());
        dto.setDescription(seller.getDescription());
        dto.setLogoUrl(seller.getLogoUrl());
        dto.setRating(seller.getRating());
        dto.setContactEmail(seller.getContactEmail());
        dto.setContactPhone(seller.getContactPhone());
        dto.setCreatedAt(seller.getCreatedAt());

        return dto;
    }
}
