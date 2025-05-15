package com.pawn.wantedcqrs.brand.service;

import com.pawn.wantedcqrs.brand.dto.BrandDto;
import com.pawn.wantedcqrs.brand.entity.Brand;
import com.pawn.wantedcqrs.brand.repository.BrandRepository;
import com.pawn.wantedcqrs.common.exception.e4xx.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;


    @Transactional(readOnly = true)
    public BrandDto getBrandBy(Long brandId) {
        Brand foundBrand = brandRepository.findById(brandId).orElseThrow(ResourceNotFoundException.BRAND::getResponseException);

        return BrandDto.fromEntity(foundBrand);
    }

}
