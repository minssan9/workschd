package com.voyagerss.persist.entity.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.dto.AccountInfoDTO;
import com.voyagerss.persist.entity.BaseEntity;
import com.voyagerss.persist.entity.EmployeeOffDates;
import com.voyagerss.persist.entity.PreferredWorkDay;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_manager")
    private Boolean manager;

    @Column(name = "preferred_branch_id", nullable = false)
    private int preferredBranchId; // 선호하는 지점 ID

    @Column(name = "employee_type")
    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType; // FULL_TIME, PART_TIME, TEMPORARY

    @OneToMany(mappedBy = "accountInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferredWorkDay> employeePreferreds;

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

    // 직원의 근무 가능 여부를 확인하는 메서드
    public boolean isAvailable(LocalDate date) {
        // 지정된 휴무일이거나, 근무할 수 없는 요일인 경우 false
        if (offDates.stream().anyMatch(offDates -> offDates.getOffDate().equals(date))  ) {
            return false;
        }
        return true;
    }

    public enum EmployeeType {
        FULL_TIME,
        PART_TIME,
        TEMPORARY
    }
}
