package com.pawn.wantedcqrs.product.controller;

import com.pawn.wantedcqrs.common.dto.response.CommonApiResponse;
import com.pawn.wantedcqrs.product.dto.MainPageDto;
import com.pawn.wantedcqrs.product.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    private final MainService mainService;

    // TODO: 다시하기
    @GetMapping("")
    public ResponseEntity<CommonApiResponse<MainPageDto>> readMainPageContents() {
        MainPageDto mainPageContents = mainService.getMainPageContents();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.ok(mainPageContents));
    }

}
