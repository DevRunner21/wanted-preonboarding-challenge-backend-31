package com.pawn.wantedcqrs.optionGroup.repository;

import com.pawn.wantedcqrs.optionGroup.dto.ProductStockProjection;

import java.util.Collection;
import java.util.List;

public interface QProductOptionGroupRepositoryCustom {
    List<ProductStockProjection> findProductStocksBy(Collection<Long> productIds);
}
