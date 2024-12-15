package com.voyagerss.persist.component.dto;

import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public final class PageReq {
    private int page;
    private int size;
    private Direction direction;

    public PageReq(int page, int size, Direction direction) {
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 1000;

        this.page = page < 0 ? 0 : page;
        this.size = (size > MAX_SIZE || size == 0)  ? DEFAULT_SIZE : size;
        this.direction = direction;
    }

    public void setPage(int page) {
        this.page = page < 0 ? 0 : page;
    }

    public void setSize(int size) {
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 1000;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of(String sortColumn) {
        return org.springframework.data.domain.PageRequest
            .of(page, size, direction, sortColumn );
    }
}
