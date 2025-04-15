package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.TeamMember;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, JpaSpecificationExecutor<TeamMember> {

  List<TeamMember> findByAccount_AccountId(@Valid @NotNull Integer accountId);
}