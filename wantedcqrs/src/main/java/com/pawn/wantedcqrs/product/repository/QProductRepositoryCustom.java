package com.pawn.wantedcqrs.product.repository;

import com.pawn.wantedcqrs.product.dto.ProductQueryCondition;
import com.pawn.wantedcqrs.product.entity.Product;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import com.pawn.wantedcqrs.product.repository.dto.ProductSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QProductRepositoryCustom {

    Page<ProductSummaryProjection> findProductSummaryPage(ProductQueryCondition filter, Pageable pageable);

    List<Product> findTop5PopularProducts();

    List<ProductSummaryProjection> findProductSummariesBy(List<Long> productIds);

}
