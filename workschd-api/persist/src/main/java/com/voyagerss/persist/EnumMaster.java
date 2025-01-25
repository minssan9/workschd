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
        STUDENT("STUDENT", "학생"),
        TEACHER("TEACHER", "선생님"),
        MANAGER("MANAGER", "관리자"),
        OWNER("OWNER", "소유자");

        private final String code;
        private final String displayName;

        public static RoleType of(String code) {
            return Arrays.stream(RoleType.values())
                    .filter(r -> r.getCode().equals(code))
                    .findAny()
                    .orElse(STUDENT);
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
    public enum EmployeeStatus {
        ACTIVE("ACTIVE", "진행 중"),
        INACTIVE("INACTIVE", "비활성"),
        NOT_PAID("NOT_PAID", "미결제"),
        END("END", "종료"),
        STOP("STOP", "중단"),
        PAUSE("PAUSE", "일시중지");

        private String employeeStatus;
        private String employeeStatusDescription;
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

    public enum BoardStatus {
        ACTIVE,
        INACTIVE
    }


}
