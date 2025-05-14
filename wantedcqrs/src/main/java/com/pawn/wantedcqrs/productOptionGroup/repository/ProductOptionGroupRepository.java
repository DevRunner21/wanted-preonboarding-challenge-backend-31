package com.pawn.wantedcqrs.productOptionGroup.repository;

import com.pawn.wantedcqrs.productOptionGroup.entity.ProductOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, Long>, QProductOptionGroupRepositoryCustom {
    List<ProductOptionGroup> findProductOptionGroupsByProductId(Long productId);
}
