package com.pawn.wantedcqrs.product.repository;

import com.pawn.wantedcqrs.product.entity.ProductImage;

import java.util.Collection;
import java.util.List;

public interface QProductImageRepository {
    List<ProductImage> findAllByProductIdsAndIsPrimary(Collection<Long> productIds);
}
