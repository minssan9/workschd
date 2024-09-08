package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.Translations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TranslationsRepository extends JpaRepository<Translations, Long>, JpaSpecificationExecutor<Translations> {

}