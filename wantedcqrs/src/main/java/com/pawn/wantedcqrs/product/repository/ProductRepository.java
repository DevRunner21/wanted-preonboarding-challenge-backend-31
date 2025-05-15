package com.pawn.wantedcqrs.product.repository;

import com.pawn.wantedcqrs.product.entity.Product;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, QProductRepositoryCustom {

    Optional<Product> findBySlug(String slug);

    List<Product> findTop5ByStatusOrderByCreatedAtDesc(ProductStatus status);

    // 카테고리별 상품 수 카운트
    @Query("SELECT c.id, COUNT(p) " +
            "FROM Product p JOIN p.categories c " +
            "WHERE p.status = 'ACTIVE' " +
            "GROUP BY c.id")
    List<Object[]> countProductsByCategories();

}
