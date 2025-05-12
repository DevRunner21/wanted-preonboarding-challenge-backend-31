package com.pawn.wantedcqrs.product.controller;

import com.pawn.wantedcqrs.common.dto.response.CommonApiResponse;
import com.pawn.wantedcqrs.common.dto.response.CustomPageResponse;
import com.pawn.wantedcqrs.common.util.PageableCreator;
import com.pawn.wantedcqrs.product.dto.CreateProductRequest;
import com.pawn.wantedcqrs.product.dto.CreateProductResponse;
import com.pawn.wantedcqrs.product.dto.ProductQueryCondition;
import com.pawn.wantedcqrs.product.dto.ProductSummaryResult;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import com.pawn.wantedcqrs.product.service.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductFacade productFacade;

    @PostMapping()
    public ResponseEntity<CommonApiResponse<CreateProductResponse>> create(@RequestBody CreateProductRequest request) {

        CreateProductResponse response = productFacade.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonApiResponse.ok(response));
    }

    @GetMapping()
    public ResponseEntity<CommonApiResponse<CustomPageResponse<ProductSummaryResult>>> readProductPages(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "created_at:desc") List<String> sort,
            @RequestParam(name = "status", required = false) ProductStatus status,
            @RequestParam(name = "minPrice", required = false) Integer minPrice,
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(name = "category", required = false) List<Long> category,
            @RequestParam(name = "seller", required = false) Long seller,
            @RequestParam(name = "brand", required = false) Long brand,
            @RequestParam(name = "inStock", required = false) Boolean inStock,
            @RequestParam(name = "search", required = false) String search
    ) {

        ProductQueryCondition productQueryFilter = ProductQueryCondition.builder()
                .status(status)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .categoryIds(category)
                .sellerId(seller)
                .brandId(brand)
                .inStock(inStock)
                .search(search)
                .build();

        Pageable pageable = PageableCreator.create(page, perPage, sort);

        Page<ProductSummaryResult> productSummaryPage = productFacade.getProductSummaryPages(productQueryFilter, pageable);

        return ResponseEntity.ok()
                .body(CommonApiResponse.ok(CustomPageResponse.of(productSummaryPage)));
    }
}
