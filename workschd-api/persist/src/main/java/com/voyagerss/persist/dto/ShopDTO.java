package com.voyagerss.persist.dto;


import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ShopDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    private String address;

    private String district;

    private String postalCode;

    private Double latitude;

    private Double longitude;

    private String phone;

    private String description;

    private Integer totalRooms;

    private String region;
 
    // Team relationship fields
    private Long teamId;
    private String teamName;

}
