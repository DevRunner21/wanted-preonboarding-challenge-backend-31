package com.pawn.wantedcqrs.product.repository;

import com.pawn.wantedcqrs.product.entity.ProductImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.pawn.wantedcqrs.product.entity.QProductImage.productImage;

@Repository
@RequiredArgsConstructor
public class QProductImageRepositoryImpl implements QProductImageRepository {

    private final JPAQueryFactory qf;

    @Override
    public List<ProductImage> findAllByProductIdsAndIsPrimary(Collection<Long> productIds) {
        return qf.selectFrom(productImage)
                .where(
                        productImage.isPrimary.eq(true),
                        productImage.productId.in(productIds)
                )
                .fetch();
    }

}
