package com.pawn.wantedcqrs.product.service;

import com.pawn.wantedcqrs.category.domain.Category;
import com.pawn.wantedcqrs.category.repository.CategoryRepository;
import com.pawn.wantedcqrs.product.dto.MainPageDto;
import com.pawn.wantedcqrs.product.dto.ProductSummaryResult;
import com.pawn.wantedcqrs.product.entity.Product;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import com.pawn.wantedcqrs.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductFacade productFacade;

    public MainPageDto getMainPageContents() {
        // 1. 신규 상품 조회 (최근 등록순 10개)
        List<Product> newProducts = productRepository.findTop5ByStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE);
        List<Long> newProductIds = newProducts.stream().map(Product::getId).toList();
        List<ProductSummaryResult> newProductSummaries = productFacade.getProductSummaryResultsBy(newProductIds);

        // 2. 인기 상품 조회 (리뷰 평점 높은순 10개)
        List<Product> popularProducts = productRepository.findTop5PopularProducts();
        List<Long> popularProductIds = popularProducts.stream().map(Product::getId).toList();
        List<ProductSummaryResult> popularProductSummaries = productFacade.getProductSummaryResultsBy(popularProductIds);


        // 3. 주요 카테고리 조회 (1단계 카테고리 중 상품이 많은 순으로 5개)
        List<Category> categories = categoryRepository.findByLevel(1);

        // 카테고리별 상품 수 카운트
        List<Object[]> categoryCountResults = productRepository.countProductsByCategories();
        Map<Long, Long> categoryProductCounts = new HashMap<>();

        for (Object[] result : categoryCountResults) {
            Long categoryId = (Long) result[0];
            Long productCount = ((Number) result[1]).longValue();
            categoryProductCounts.put(categoryId, productCount);
        }

        List<MainPageDto.FeaturedCategory> featuredCategories = categories.stream()
                .map(category -> {
                    long productCount = categoryProductCounts.getOrDefault(category.getId(), 0L);
                    return MainPageDto.FeaturedCategory.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .slug(category.getSlug())
                            .imageUrl(category.getImageUrl())
                            .productCount((int) productCount)
                            .build();
                })
                .filter(c -> c.getProductCount() > 0) // 상품이 있는 카테고리만 필터링
                .sorted((c1, c2) -> c2.getProductCount() - c1.getProductCount())
                .limit(5)
                .collect(Collectors.toList());

        // 결과 DTO 생성 및 반환
        return MainPageDto.builder()
                .newProducts(newProductSummaries)
                .popularProducts(popularProductSummaries)
                .featuredCategories(featuredCategories)
                .build();
    }

}
