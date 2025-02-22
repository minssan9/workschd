package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

    Optional<Team> findByInvitationHash(String hash);
    Optional<Team> findByName(String name);
}