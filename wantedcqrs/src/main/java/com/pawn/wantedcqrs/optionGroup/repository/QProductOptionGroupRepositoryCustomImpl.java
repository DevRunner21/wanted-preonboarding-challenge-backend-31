package com.pawn.wantedcqrs.optionGroup.repository;

import com.pawn.wantedcqrs.optionGroup.dto.ProductStockProjection;
import com.pawn.wantedcqrs.optionGroup.dto.QProductStockProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.pawn.wantedcqrs.optionGroup.entity.QProductOption.productOption;
import static com.pawn.wantedcqrs.optionGroup.entity.QProductOptionGroup.productOptionGroup;

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
