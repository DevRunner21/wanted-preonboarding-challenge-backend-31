package com.pawn.wantedcqrs.category.service;

import com.pawn.wantedcqrs.category.dto.CategoryDto;
import com.pawn.wantedcqrs.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategoriesBy(List<Long> categoryIds) {
        return categoryRepository.findCategoriesByIdIsIn(categoryIds).stream()
                .map(CategoryDto::fromEntity).collect(Collectors.toList());
    }

}
