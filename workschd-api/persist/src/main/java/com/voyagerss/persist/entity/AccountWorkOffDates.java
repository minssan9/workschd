package com.voyagerss.persist.entity;

import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.dto.BaseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "account_work_off_dates")
@Getter
@Setter
@NoArgsConstructor
public class AccountWorkOffDates extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "off_date", nullable = false)
    private LocalDate offDate;

    public void updateFromDto(AccountWorkOffDatesDTO dto) {
        if (dto != null) {
            LocalDate newOffDate = dto.getOffDateAsLocalDate();
            if (newOffDate != null) {
                this.offDate = newOffDate;
            }
        }
    }

    public AccountWorkOffDates(AccountWorkOffDatesDTO dto) {
        updateFromDto(dto);
    }
}
