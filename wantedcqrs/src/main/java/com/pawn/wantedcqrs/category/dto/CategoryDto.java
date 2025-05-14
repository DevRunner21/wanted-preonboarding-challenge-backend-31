package com.pawn.wantedcqrs.category.dto;

import com.pawn.wantedcqrs.category.domain.Category;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryDto {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private CategoryDto parent;

    private Integer level;

    private String imageUrl;

    public static CategoryDto fromEntity(Category category) {
        if (category == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setLevel(category.getLevel());
        dto.setImageUrl(category.getImageUrl());

        // parent는 한 단계만 변환하여 순환 방지
        if (category.getParent() != null) {
            Category parent = category.getParent();
            CategoryDto parentDto = new CategoryDto();
            parentDto.setId(parent.getId());
            parentDto.setName(parent.getName());
            parentDto.setSlug(parent.getSlug());
            parentDto.setDescription(parent.getDescription());
            parentDto.setLevel(parent.getLevel());
            parentDto.setImageUrl(parent.getImageUrl());
            // parentDto.setParent(null); // 명시적으로 생략 가능
            dto.setParent(parentDto);
        }

        return dto;
    }

    public static List<CategoryDto> fromEntities(List<Category> categories) {
        if (categories == null) return Collections.emptyList();
        return categories.stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

}
