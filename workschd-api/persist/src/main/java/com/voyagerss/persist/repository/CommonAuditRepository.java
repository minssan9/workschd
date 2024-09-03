package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.CommonAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommonAuditRepository extends JpaRepository<CommonAudit, Long>, JpaSpecificationExecutor<CommonAudit> {

}