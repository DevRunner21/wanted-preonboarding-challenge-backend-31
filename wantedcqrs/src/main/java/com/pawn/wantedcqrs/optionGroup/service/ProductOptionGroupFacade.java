package com.pawn.wantedcqrs.optionGroup.service;

import com.pawn.wantedcqrs.optionGroup.dto.*;
import com.pawn.wantedcqrs.product.dto.ProductDto;
import com.pawn.wantedcqrs.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductOptionGroupFacade {

    private final ProductOptionGroupService productOptionGroupService;
    private final ProductService productService;


    @Transactional
    public CreateOptionResponse createOption(Long productId, CreateOptionRequest request) {
        ProductDto foundProduct = productService.getProductBy(productId);

        ProductOptionDto newOption = productOptionGroupService.saveOption(foundProduct.getId(), request.toProductOptionDto());

        return CreateOptionResponse.builder()
                .id(newOption.getId())
                .optionGroupId(newOption.getOptionGroupId())
                .name(newOption.getName())
                .sku(newOption.getSku())
                .additionalPrice(newOption.getAdditionalPrice())
                .displayOrder(newOption.getDisplayOrder())
                .stock(newOption.getStock())
                .build();
    }

    @Transactional
    public UpdateOptionResponse updateOption(Long productId, Long optionId, UpdateOptionRequest request) {
        ProductDto foundProduct = productService.getProductBy(productId);
        ProductOptionDto productOptionDto = request.toProductOptionDto();

        ProductOptionDto updatedOption = productOptionGroupService.updateOption(foundProduct.getId(), optionId, productOptionDto);

        return UpdateOptionResponse.builder()
                .id(updatedOption.getId())
                .optionGroupId(updatedOption.getOptionGroupId())
                .additionalPrice(updatedOption.getAdditionalPrice())
                .sku(updatedOption.getSku())
                .stock(updatedOption.getStock())
                .displayOrder(updatedOption.getDisplayOrder())
                .name(updatedOption.getName())
                .build();
    }

}
