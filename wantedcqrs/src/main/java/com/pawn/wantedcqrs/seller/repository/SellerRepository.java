package com.pawn.wantedcqrs.seller.repository;

import com.pawn.wantedcqrs.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
