package com.pawn.wantedcqrs.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_tags")
public class ProductTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    //    tag_id: 태그 ID (FK)
    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Builder
    private ProductTag(Long id, Product product, Long tagId) {
        this.id = id;
        this.product = product;
        this.tagId = tagId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
