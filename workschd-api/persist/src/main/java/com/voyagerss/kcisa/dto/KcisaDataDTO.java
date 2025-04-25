package com.voyagerss.kcisa.dto;

import com.voyagerss.kcisa.entity.KcisaData;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class KcisaDataDTO {
    private Long id;
    private String title;
    private String description;
    private String performanceType;
    private String eventDate;
    private String venue;
    private String period;
    private Integer eventPeriod;
    private String charge;
    private String contactPoint;
    private String url;
    private String imageObject;
    private String type;
    private Integer viewCount;
    private String apiId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public KcisaDataDTO(KcisaData entity) {
        BeanUtils.copyProperties(entity, this);
    }


    public KcisaDataDTO(String 기본값, String s, String s1, String s2, String s3, String s4, String apiId, String 기본값1, String 기본값2) {
        this.title = 기본값;
        this.description = s;
        this.performanceType = s1;
        this.eventDate = s2;
        this.venue = s3;
        this.period = s4;
        this.apiId = apiId;
        this.type = 기본값1;
        this.imageObject = 기본값2;
    }
}
