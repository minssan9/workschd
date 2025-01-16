package com.voyagerss.persist.entity;

import com.voyagerss.persist.entity.account.AccountInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "unavailable_dates")
public class EmployeeOffDates extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_info_id")
    private AccountInfo accountInfo;

    @Column(name = "off_date", nullable = false)
    private LocalDate offDate;

}
