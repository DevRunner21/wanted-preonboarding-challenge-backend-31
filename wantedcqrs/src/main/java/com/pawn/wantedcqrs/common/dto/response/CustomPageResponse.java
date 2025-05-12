package com.pawn.wantedcqrs.common.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class CustomPageResponse<T> {

    private final CustomPagination pagination;

    private final List<T> items;

    protected CustomPageResponse(CustomPagination pagination, List<T> items) {
        this.pagination = pagination;
        this.items = items;
    }

    public static <T> CustomPageResponse<T> of(Page<T> page) {
        return new CustomPageResponse<>(
                new CustomPagination(page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize())
                , page.getContent()
        );
    }

    @Getter
    public static class CustomPagination {

        private final Long totalCount;

        private final int totalPages;

        private final int pageIndex;

        private final int pageSize;

        protected CustomPagination(Long totalCount, int totalPages, int pageIndex, int pageSize) {
            this.totalCount = totalCount;
            this.totalPages = totalPages;
            this.pageIndex = pageIndex;
            this.pageSize = pageSize;
        }

    }

}
