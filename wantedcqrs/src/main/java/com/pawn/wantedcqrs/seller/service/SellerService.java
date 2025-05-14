package com.pawn.wantedcqrs.seller.service;

import com.pawn.wantedcqrs.common.exception.e4xx.ResourceNotFoundException;
import com.pawn.wantedcqrs.seller.dto.SellerDto;
import com.pawn.wantedcqrs.seller.entity.Seller;
import com.pawn.wantedcqrs.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerService {

    private final SellerRepository sellerRepository;

    @Transactional(readOnly = true)
    public SellerDto getSellerBy(Long sellerId) {
        Seller foundSeller = sellerRepository.findById(sellerId).orElseThrow(ResourceNotFoundException.Seller::getResponseException);

        return SellerDto.fromEntity(foundSeller);
    }

}
