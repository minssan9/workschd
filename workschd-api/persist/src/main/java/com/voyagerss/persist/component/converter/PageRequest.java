package com.voyagerss.persist.component.converter;

import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.EnumMaster.*;
import com.voyagerss.persist.component.exception.CommonException;
import com.voyagerss.persist.component.exception.CommonExceptionType;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public final class PageRequest {

    private static final String CREATED_DATETIME = "createdDatetime";
    private static final String VIEW_COUNT = "viewCount";

    private int page = 0;
    private int size = 20;
    private EnumMaster.SortType sortType;

    public void setPage(int page) {
        this.page = Math.max(page, 0);
    }

    public void setSize(int size) {
        int DEFAULT_SIZE = 20;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setSortType(EnumMaster.SortType sortType) {
        this.sortType = sortType;
    }

    public org.springframework.data.domain.PageRequest of(EnumMaster.PagingType pagingType) {
        // sortType을 입력 받지 않았거나, 입력 받은 값이 SortType에 포함되지 않는 값이라면 'NEWEST'로 기본값 세팅
        sortType = sortType == null ? EnumMaster.SortType.NEWEST : sortType;

        // 신규 PagingType이 추가되면 추가검증 필요
        validate(pagingType);

        switch (sortType) {
            case OLDEST:
                return org.springframework.data.domain.PageRequest
                    .of(page, size, Sort.by(Order.asc(CREATED_DATETIME)));
            case VIEW_COUNT:
                return org.springframework.data.domain.PageRequest
                    .of(page, size, Sort.by(Order.desc(VIEW_COUNT)));
            default: // NEWEST
                return org.springframework.data.domain.PageRequest
                    .of(page, size, Sort.by(Order.desc(CREATED_DATETIME)));
        }
    }

    private void validate(PagingType pagingType) {
        if (pagingType.equals(PagingType.DEFAULT) && !sortType.equals(SortType.NEWEST)) {
            throw new CommonException(CommonExceptionType.UNSUPPORTED_SORT_TYPE,
                ", sortType : " +  sortType);
        }
    }
}
