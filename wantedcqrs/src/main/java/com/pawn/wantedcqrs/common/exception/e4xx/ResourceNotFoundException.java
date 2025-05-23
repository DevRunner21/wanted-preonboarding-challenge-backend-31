package com.pawn.wantedcqrs.common.exception.e4xx;

import com.pawn.wantedcqrs.common.exception.ResponseDefinition;
import com.pawn.wantedcqrs.common.exception.ResponseException;
import org.springframework.http.HttpStatus;

public enum ResourceNotFoundException implements ResponseDefinition {

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 리소스를 찾을 수 없음")
    , PRODUCT(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 상품을 찾을 수 없음")
    , SELLER(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 Seller를 찾을 수 없음")
    , BRAND(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 Brand를 찾을 수 없음")
    , CATEGORY(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 Category를 찾을 수 없음")
    , TAG(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 TAG를 찾을 수 없음")
    , USER(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 USER를 찾을 수 없음")
    , REVIEW(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 REVIEW를 찾을 수 없음")
    , OPTION_GROUP(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 OptionGroup을 찾을 수 없음");

    private final ResponseException responseException;

    ResourceNotFoundException(HttpStatus status, String code, String message) {
        this.responseException = new ResponseException(status, code, message);
    }

    @Override
    public ResponseException getResponseException() {
        return responseException;
    }

}
