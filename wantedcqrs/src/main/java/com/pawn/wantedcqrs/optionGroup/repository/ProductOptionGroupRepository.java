package com.pawn.wantedcqrs.optionGroup.repository;

import com.pawn.wantedcqrs.optionGroup.entity.ProductOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, Long>, QProductOptionGroupRepositoryCustom {
    List<ProductOptionGroup> findProductOptionGroupsByProductId(Long productId);
}
