package com.voyagerss.persist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.voyagerss.persist.component.converter.IntegerArrayConverter;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.dto.AccountInfoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "account_info")
public class AccountInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "preferred_day")
    private String preferredDay; // 선호 요일

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_manager")
    private Boolean manager;

    @Column(name = "preferred_branch_id", nullable = false)
    private int preferredBranchId; // 선호하는 지점 ID


    @Column(name = "unavailable_days_of_week")
    @Convert(converter = IntegerArrayConverter.class)
    private Set<Integer> offDaysOfWeek; // 직원이 근무할 수 없는 요일들 (예: "SATURDAY", "SUNDAY")

    @OneToMany(mappedBy = "accountInfo", fetch = FetchType.LAZY)
    private List<EmployeeOffDates> offDates; // 휴무 일자로 지정된 날들


    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public AccountInfo() {
    }

    public AccountInfo(AccountDTO accountDTO) {
        BeanUtils.copyProperties(accountDTO, this);
    }

    public void setAccountInfo(AccountInfoDTO vO) {
    }

    // 직원의 선호 요일을 기준으로 근무 가능 여부 확인
    public boolean isPreferredDay(LocalDate date) {
        return date.getDayOfWeek().toString().equals(preferredDay);
    }

    // 휴무할 수 없는 요일 추가 (예: 주말)
    public void addUnavailableDayOfWeek(Integer dayOfWeek) {
        offDaysOfWeek.add(dayOfWeek);
    }


    // 직원의 근무 가능 여부를 확인하는 메서드
    public boolean isAvailable(LocalDate date) {
        // 지정된 휴무일이거나, 근무할 수 없는 요일인 경우 false
        if (offDates.stream().anyMatch(offDates -> offDates.getOffDate().equals(date)) ||
                offDaysOfWeek.contains(date.getDayOfWeek().toString())) {
            return false;
        }
        return true;
    }


//
//    private int totalWorkDays; // 근무 일수 추적
//    private int totalOffDays;  // 휴무 일수
//    // 휴무일 초과 여부 확인
//    public boolean hasExceededOffDays(int maxOffDays) {
//        return totalOffDays >= maxOffDays;
//    }
//
//    // 직원이 근무한 총 일수 반환
//    public int getTotalWorkDays() {
//        return totalWorkDays;
//    }

}
