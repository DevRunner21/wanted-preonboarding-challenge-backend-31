package com.pawn.wantedcqrs.product.repository;

import com.pawn.wantedcqrs.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>, QProductImageRepository {
}
