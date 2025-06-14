package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.AccountWorkOffDates;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AccountWorkOffDatesDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private Long accountId;

    private LocalDate offDate;  // ISO format date string


    public AccountWorkOffDatesDTO(AccountWorkOffDates entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.accountId = entity.getAccount().getAccountId().longValue();
            this.offDate = entity.getOffDate() != null ? entity.getOffDate() : null;
        }
    }

    public LocalDate getOffDateAsLocalDate() {
        return offDate != null ? offDate : null;
    }
}
