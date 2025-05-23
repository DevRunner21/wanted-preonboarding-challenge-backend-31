package com.pawn.wantedcqrs.optionGroup.service;

import com.pawn.wantedcqrs.common.exception.e4xx.ResourceNotFoundException;
import com.pawn.wantedcqrs.optionGroup.dto.ProductOptionDto;
import com.pawn.wantedcqrs.optionGroup.dto.ProductOptionGroupDto;
import com.pawn.wantedcqrs.optionGroup.dto.ProductStockProjection;
import com.pawn.wantedcqrs.optionGroup.entity.ProductOption;
import com.pawn.wantedcqrs.optionGroup.entity.ProductOptionGroup;
import com.pawn.wantedcqrs.optionGroup.repository.ProductOptionGroupRepository;
import com.pawn.wantedcqrs.optionGroup.repository.ProductOptionRepository;
import com.pawn.wantedcqrs.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductOptionGroupService {

    private final ProductOptionGroupRepository productOptionGroupRepository;

    private final ProductOptionRepository productOptionRepository;

    @Transactional
    public List<ProductOptionGroupDto> createProductOptionGroupsByProductId(Long productId, List<ProductOptionGroupDto> productOptionGroupParams) {

        List<ProductOptionGroup> productOptionGroups = productOptionGroupParams.stream().map(dto -> ProductOptionGroup.builder()
                .productId(productId)
                .name(dto.getName())
                .displayOrder(dto.getDisplayOrder())
                .options(dto.getOptions().stream()
                        .map(ProductOptionDto::toEntity)
                        .collect(Collectors.toList())
                ).build()
        ).toList();
        List<ProductOptionGroup> savedProductOptionGroups = productOptionGroupRepository.saveAll(productOptionGroups);

        return savedProductOptionGroups.stream()
                .map(ProductOptionGroupDto::fromEntity)
                .toList();
    }

    public Map<Long, ProductStockProjection> getProductStockMapBy(List<Long> productIds) {
        return productOptionGroupRepository.findProductStocksBy(productIds).stream()
                .collect(Collectors.toMap(
                        ProductStockProjection::getProductId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    public List<ProductOptionGroupDto> getOptionGroupsBy(Long productId) {
        List<ProductOptionGroup> foundProductOptionGroups = productOptionGroupRepository.findProductOptionGroupsByProductId((productId));

        return foundProductOptionGroups.stream()
                .map(ProductOptionGroupDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductOptionDto saveOption(Long productId, ProductOptionDto productOptionDto) {

        ProductOptionGroup foundOptionGroup = productOptionGroupRepository.findProductOptionGroupByIdAndProductId(productOptionDto.getOptionGroupId(), productId)
                .orElseThrow(ResourceNotFoundException.OPTION_GROUP::getResponseException);

        ProductOption newOption = ProductOption.builder()
                .name(productOptionDto.getName())
                .sku(productOptionDto.getSku())
                .additionalPrice(productOptionDto.getAdditionalPrice())
                .stock(productOptionDto.getStock())
                .displayOrder(productOptionDto.getDisplayOrder())
                .build();

        foundOptionGroup.addOption(newOption);
//        ProductOptionGroup optionGroup = productOptionGroupRepository.save(foundOptionGroup);
        newOption.setOptionGroup(foundOptionGroup);
        ProductOption savedOption = productOptionRepository.save(newOption);

        return ProductOptionDto.fromEntity(savedOption);
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        List<ProductOptionGroup> optionGroups = productOptionGroupRepository.findProductOptionGroupsByProductId((productId));

        Map<Long, ProductOption> optionMap = optionGroups.stream()
                .flatMap(group -> {
                    List<ProductOption> options = group.getOptions();
                    return options == null ? Stream.empty() : options.stream();
                })
                .collect(Collectors.toMap(
                        ProductOption::getId,
                        Function.identity()
                ));

        if (!optionMap.containsKey(optionId)) {
            throw ResourceNotFoundException.OPTION.getResponseException();
        }

        ProductOption target = optionMap.get(optionId);
        ProductOptionGroup optionGroup = target.getOptionGroup();
        optionGroup.removeOption(target);
    }

}
