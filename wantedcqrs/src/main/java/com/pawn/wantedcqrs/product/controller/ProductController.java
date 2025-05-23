package com.pawn.wantedcqrs.product.controller;

import com.pawn.wantedcqrs.common.dto.response.CommonApiResponse;
import com.pawn.wantedcqrs.common.dto.response.CustomPageResponse;
import com.pawn.wantedcqrs.common.util.PageableCreator;
import com.pawn.wantedcqrs.product.dto.*;
import com.pawn.wantedcqrs.product.entity.ProductStatus;
import com.pawn.wantedcqrs.product.service.ProductFacade;
import com.pawn.wantedcqrs.product.service.ProductService;
import jakarta.validation.constraints.Positive;
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

    private final ProductService productService;

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

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<ProductDetailResponse>> readProductDetail(@Positive @PathVariable("id") Long productId) {
        ProductDetailResponse detail = productFacade.getProductDetail(productId);

        return ResponseEntity.ok()
                .body(CommonApiResponse.ok(detail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<UpdateProductResponse>> update(
            @Positive @PathVariable("id") Long productId
            , @RequestBody UpdateProductRequest request) {

        UpdateProductResponse response = productFacade.updateProduct(productId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.ok(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse> delete(@PathVariable("id") Long productId) {

        productService.delete(productId);

        return ResponseEntity.ok(CommonApiResponse.ok(null));
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<CommonApiResponse<CreateProductImageResponse>> createProductImage(
            @PathVariable("productId") Long productId,
            @RequestBody CreateProductImageRequest request
    ) {
        CreateProductImageResponse response = productFacade.addProductImage(productId, request);

        return ResponseEntity.ok(CommonApiResponse.ok(response));
    }

}
