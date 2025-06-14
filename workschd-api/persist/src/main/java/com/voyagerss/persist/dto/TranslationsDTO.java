package com.voyagerss.persist.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class TranslationsDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String txtKey;

    private String en;

    private String ko;

    private String fr;

    private String es;

    private String ja;

}
