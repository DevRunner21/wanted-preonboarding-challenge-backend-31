package com.pawn.wantedcqrs.productOptionGroup.repository;

import com.pawn.wantedcqrs.productOptionGroup.dto.ProductStockProjection;
import com.pawn.wantedcqrs.productOptionGroup.dto.QProductStockProjection;
import com.pawn.wantedcqrs.productOptionGroup.entity.QProductOption;
import com.pawn.wantedcqrs.productOptionGroup.entity.QProductOptionGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.pawn.wantedcqrs.productOptionGroup.entity.QProductOption.productOption;
import static com.pawn.wantedcqrs.productOptionGroup.entity.QProductOptionGroup.productOptionGroup;

@Repository
@RequiredArgsConstructor
public class QProductOptionGroupRepositoryCustomImpl implements QProductOptionGroupRepositoryCustom {

    private final JPAQueryFactory qf;


    @Override
    public List<ProductStockProjection> findProductStocksBy(Collection<Long> productIds) {
        return qf.select(new QProductStockProjection(
                        productOptionGroup.productId,
                        productOption.stock.sum()
                ))
                .from(productOptionGroup)
                .join(productOptionGroup.options, productOption)
                .where(productOptionGroup.productId.in(productIds))
                .groupBy(productOptionGroup.productId)
                .fetch();
    }

}
