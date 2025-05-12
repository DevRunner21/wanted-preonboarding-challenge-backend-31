package com.pawn.wantedcqrs.category.repository;

import com.pawn.wantedcqrs.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
