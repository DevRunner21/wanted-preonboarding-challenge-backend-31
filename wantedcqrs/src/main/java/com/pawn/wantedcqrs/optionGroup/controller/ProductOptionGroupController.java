package com.pawn.wantedcqrs.optionGroup.controller;

import com.pawn.wantedcqrs.common.dto.response.CommonApiResponse;
import com.pawn.wantedcqrs.optionGroup.dto.CreateOptionRequest;
import com.pawn.wantedcqrs.optionGroup.dto.CreateOptionResponse;
import com.pawn.wantedcqrs.optionGroup.dto.UpdateOptionRequest;
import com.pawn.wantedcqrs.optionGroup.dto.UpdateOptionResponse;
import com.pawn.wantedcqrs.optionGroup.service.ProductOptionGroupFacade;
import com.pawn.wantedcqrs.optionGroup.service.ProductOptionGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductOptionGroupController {

    private final ProductOptionGroupFacade productOptionGroupFacade;

    private final ProductOptionGroupService productOptionGroupService;


    @PostMapping("/products/{productId}/options")
    public ResponseEntity<CommonApiResponse<CreateOptionResponse>> createOption(@PathVariable Long productId, @RequestBody CreateOptionRequest request) {
        CreateOptionResponse response = productOptionGroupFacade.createOption(productId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonApiResponse.ok(response));
    }

    @DeleteMapping("/products/{productId}/options/{optionId}")
    public ResponseEntity<CommonApiResponse> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        productOptionGroupService.deleteOption(productId, optionId);

        return ResponseEntity.ok(CommonApiResponse.ok(null));
    }

    @PutMapping("/products/{productId}/options/{optionId}")
    public ResponseEntity<CommonApiResponse<UpdateOptionResponse>> updateOption(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            @RequestBody UpdateOptionRequest request
    ) {
        UpdateOptionResponse response = productOptionGroupFacade.updateOption(productId, optionId, request);

        return ResponseEntity.ok(CommonApiResponse.ok(response));
    }

}
