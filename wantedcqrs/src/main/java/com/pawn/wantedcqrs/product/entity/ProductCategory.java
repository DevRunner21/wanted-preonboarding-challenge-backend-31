package com.pawn.wantedcqrs.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_categories")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    private Long categoryId;

    //    is_primary: 주요 카테고리 여부
    @Column(name = "is_primary")
    private boolean isPrimary = false;

    @Builder
    private ProductCategory(Long id, Product product, Long categoryId, boolean isPrimary) {
        this.id = id;
        this.product = product;
        this.categoryId = categoryId;
        this.isPrimary = isPrimary;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
