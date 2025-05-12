package com.pawn.wantedcqrs.productOptionGroup.repository;

import com.pawn.wantedcqrs.productOptionGroup.dto.ProductStockProjection;

import java.util.Collection;
import java.util.List;

public interface QProductOptionGroupRepositoryCustom {
    List<ProductStockProjection> findProductStocksBy(Collection<Long> productIds);
}
