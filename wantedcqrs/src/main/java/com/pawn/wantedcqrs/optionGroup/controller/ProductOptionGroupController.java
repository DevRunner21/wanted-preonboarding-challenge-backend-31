package com.pawn.wantedcqrs.optionGroup.controller;

import com.pawn.wantedcqrs.common.dto.response.CommonApiResponse;
import com.pawn.wantedcqrs.optionGroup.dto.CreateOptionRequest;
import com.pawn.wantedcqrs.optionGroup.dto.CreateOptionResponse;
import com.pawn.wantedcqrs.optionGroup.service.ProductOptionGroupFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductOptionGroupController {

    private final ProductOptionGroupFacade productOptionGroupFacade;


    @PostMapping("/products/{productId}/options")
    public ResponseEntity<CommonApiResponse<CreateOptionResponse>>  createOption(@PathVariable Long productId, @RequestBody CreateOptionRequest request) {
        CreateOptionResponse response = productOptionGroupFacade.createOption(productId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonApiResponse.ok(response));
    }

}
