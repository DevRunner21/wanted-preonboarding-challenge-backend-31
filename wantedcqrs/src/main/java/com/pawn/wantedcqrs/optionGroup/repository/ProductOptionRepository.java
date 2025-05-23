package com.pawn.wantedcqrs.optionGroup.repository;

import com.pawn.wantedcqrs.optionGroup.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
