package com.voyagerss.persist.dto;

import java.util.List;

public record KakaoAddressResponse(
    List<KakaoAddressDocument> documents,
    Meta meta
) {
    public record Meta(
        Integer total_count,
        Integer pageable_count,
        Boolean is_end
    ) {}
} 