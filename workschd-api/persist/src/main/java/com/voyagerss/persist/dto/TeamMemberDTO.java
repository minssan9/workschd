package com.voyagerss.persist.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TeamMemberDTO extends BaseDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime joinDate;
    private String status;
    private Integer priority;

    private Integer accountId;

}