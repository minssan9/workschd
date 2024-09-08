package com.voyagerss.persist.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "translations")
public class Translations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "txt_key", nullable = false)
    private String txtKey;

    @Column(name = "en")
    private String en;

    @Column(name = "ko")
    private String ko;

    @Column(name = "fr")
    private String fr;

    @Column(name = "es")
    private String es;

    @Column(name = "ja")
    private String ja;

}
