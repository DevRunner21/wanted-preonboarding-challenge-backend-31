package com.pawn.wantedcqrs.optionGroup.repository;

import com.pawn.wantedcqrs.optionGroup.entity.ProductOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, Long>, QProductOptionGroupRepositoryCustom {
    List<ProductOptionGroup> findProductOptionGroupsByProductId(Long productId);

    Optional<ProductOptionGroup> findProductOptionGroupByIdAndProductId(Long id, Long productId);
}
