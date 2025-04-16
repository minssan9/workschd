package com.voyagerss.persist;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class EnumMaster {

    @Getter
    @RequiredArgsConstructor
    public enum RoleType {
        WORKER("WORKER", "근로자"),
        SCHEDULER("SCHEDULER", "스케쥴러"),
        MANAGER("MANAGER", "관리자"),
        OWNER("OWNER", "소유자");

        private final String code;
        private final String displayName;

        public static RoleType of(String code) {
            return Arrays.stream(RoleType.values())
                    .filter(r -> r.getCode().equals(code))
                    .findAny()
                    .orElse(WORKER);
        }
    }


    @RequiredArgsConstructor
    @Getter
    public enum ProviderType {
        GOOGLE("GOOGLE"),
        FACEBOOK("FACEBOOK"),
        NAVER("NAVER"),
        KAKAO("KAKAO"),
        LOCAL("LOCAL");
        private final String code;
    }

    @Getter
    public enum AttendStatus {
        NO_ATTEND("결근", false),
        ATTEND("출근", true),
        LATE("지각", true);

        private String displayStatus;
        private Boolean countFlag;

        AttendStatus(String displayStatus, Boolean countFlag) {
            this.displayStatus = displayStatus;
            this.countFlag = countFlag;
        }
    }


    @Getter
    @AllArgsConstructor
    public enum AccountStatus {
        ACTIVE("ACTIVE", "진행 중"),
        INACTIVE("INACTIVE", "비활성"),
        STOP("STOP", "중단"),
        PAUSE("PAUSE", "일시중지");

        private String employeeStatus;
        private String employeeStatusDescription;
    }

    /**
     * Task status enum representing different states of a task
     */
    @Getter
    @RequiredArgsConstructor
    public enum TaskStatus {
        SCHEDULED("SCHEDULED", "예정됨", "blue"),
        IN_PROGRESS("IN_PROGRESS", "진행중", "green"),
        COMPLETED("COMPLETED", "완료됨", "purple"),
        CANCELLED("CANCELLED", "취소됨", "grey");

        private final String code;
        private final String displayName;
        private final String color;

        /**
         * Find a TaskStatus by its code
         * @param code The status code to look for
         * @return The matching TaskStatus or SCHEDULED if not found
         */
        public static TaskStatus of(String code) {
            return Arrays.stream(TaskStatus.values())
                    .filter(s -> s.getCode().equals(code))
                    .findAny()
                    .orElse(SCHEDULED);
        }
    }

    /**
     * Task employee status enum for managing task assignments
     */
    @Getter
    @RequiredArgsConstructor
    public enum TaskEmployeeStatus {
        PENDING("PENDING", "대기중", "orange"),
        APPROVED("APPROVED", "승인됨", "green"),
        REJECTED("REJECTED", "거부됨", "red"),
        ACTIVE("ACTIVE", "활성", "blue"),
        INACTIVE("INACTIVE", "비활성", "grey");

        private final String code;
        private final String displayName;
        private final String color;

        /**
         * Find a TaskEmployeeStatus by its code
         * @param code The status code to look for
         * @return The matching TaskEmployeeStatus or PENDING if not found
         */
        public static TaskEmployeeStatus of(String code) {
            return Arrays.stream(TaskEmployeeStatus.values())
                    .filter(s -> s.getCode().equals(code))
                    .findAny()
                    .orElse(PENDING);
        }
    }

    @Getter
    public enum CommonStatus {
        ACTIVE, INACTIVE
    }

    public enum PagingType {
        DEFAULT, POST
    }

    public enum SortType {
        NEWEST, OLDEST, VIEW_COUNT
    }

    @RequiredArgsConstructor
    public enum PaymentType{
        TOSSPAY("tossPay"),
        KAKAOPAY("kakaoPay"),
        PAYAPP("payApp"),
        BOOTPAY("bootPay"),
        NAVERPAY("naverPay");

        private final String payType;
    }


    @Getter
    @RequiredArgsConstructor
    public enum TemplateCode {
        MANAGER_CLASS_REGISTERED,
        MANAGER_SERVER_PROD_STATUS,

        WORKER_HOLIDAY,

        WORKER_ATTENDANCE_SHEET,
        WORKER_CLASS_PAUSE,
        WORKER_ATTEND_CHANGE
    }
 


}
