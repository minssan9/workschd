package com.voyagerss.persist.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class EmployeeOffDaysDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private Long employeeId;

    private LocalDate offDate;

}
