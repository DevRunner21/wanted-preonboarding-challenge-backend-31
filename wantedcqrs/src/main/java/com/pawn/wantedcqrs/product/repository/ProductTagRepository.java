package com.pawn.wantedcqrs.product.repository;

import com.pawn.wantedcqrs.product.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
}
