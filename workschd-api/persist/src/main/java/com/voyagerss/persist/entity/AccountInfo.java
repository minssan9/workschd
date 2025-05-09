package com.voyagerss.persist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.voyagerss.persist.component.converter.IntegerArrayConverter;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.dto.AccountInfoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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


    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;


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
