package com.pawn.wantedcqrs.product.repository;

import com.pawn.wantedcqrs.product.dto.ProductQueryCondition;
import com.pawn.wantedcqrs.product.repository.dto.ProductSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QProductRepositoryCustom {

    Page<ProductSummaryProjection> findProductSummaryPage(ProductQueryCondition filter, Pageable pageable);

}
