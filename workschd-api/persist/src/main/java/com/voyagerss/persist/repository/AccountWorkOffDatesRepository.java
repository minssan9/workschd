package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountWorkHour;
import com.voyagerss.persist.entity.AccountWorkOffDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountWorkOffDatesRepository extends JpaRepository<AccountWorkOffDates, Long>, JpaSpecificationExecutor<AccountWorkOffDates> {

  List<AccountWorkOffDates> findByAccountInfoId(Long id);
}