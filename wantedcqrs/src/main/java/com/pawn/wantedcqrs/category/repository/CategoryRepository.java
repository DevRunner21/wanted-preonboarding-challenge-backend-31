package com.pawn.wantedcqrs.category.repository;

import com.pawn.wantedcqrs.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByIdIsIn(Collection<Long> ids);
}
