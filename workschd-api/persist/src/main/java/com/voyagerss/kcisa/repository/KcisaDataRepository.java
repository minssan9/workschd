package com.voyagerss.kcisa.repository;

import com.voyagerss.kcisa.entity.KcisaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KcisaDataRepository extends JpaRepository<KcisaData, Long>, JpaSpecificationExecutor<KcisaData> {
    
    Optional<KcisaData> findByApiId(String apiId);
    
    boolean existsByApiId(String apiId);
}
