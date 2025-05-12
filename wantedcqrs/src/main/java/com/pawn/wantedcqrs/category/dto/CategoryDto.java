package com.pawn.wantedcqrs.category.dto;

import com.pawn.wantedcqrs.category.domain.Category;
import lombok.Data;

@Data
public class CategoryDto {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private Long parentId;

    private Integer level;

    private String imageUrl;

    public Category toEntity() {
        return Category.builder()
                .id(id)
                .name(name)
                .slug(slug)
                .description(description)
                .parentId(parentId)
                .level(level)
                .imageUrl(imageUrl)
                .build();
    }

}
