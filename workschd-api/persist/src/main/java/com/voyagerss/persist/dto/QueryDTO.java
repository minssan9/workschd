package com.voyagerss.persist.dto;

import com.voyagerss.persist.EnumMaster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class QueryDTO {
    private Pageable pageable;
    private String originAuth;

    private String phone;
    private String email;

    private EnumMaster.RoleType role;
    private String username;

    private Integer registerId;
    private Integer assignId;
    private Integer attendId;

    private Integer courseId;
    private Integer contentId;

    private Long accountId;
    private Integer teacherId;
    private Integer studentId;

    private String orderId;

    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate endDate;

    private LocalDate assignDate;
    private LocalDate attendDate;
    @DateTimeFormat(pattern = "HH:mm:ss") private LocalTime classStartTime;
    private LocalDateTime attendStartTime;
    private LocalDateTime attendEndTime;

    private String assignStatusString;
    private EnumMaster.AttendStatus attendStatus;
    private String attendStatusString;
    private String registerStatusString;

    private String courseName;
    private String contentName;
    private String teacherName;
    private String studentName;



    public QueryDTO() {
        this.pageable = PageRequest.of(1, 50);
    }
}
