package com.voyagerss.persist.repository;

import java.util.List;

import com.voyagerss.persist.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.voyagerss.persist.entity.Team;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {

    /**
     * Find all stores by team
     * @param team The team entity
     * @return List of stores belonging to the team
     */
    List<Shop> findByTeam(Team team);
    
    /**
     * Find all stores by team ID
     * @param teamId The team ID
     * @return List of stores belonging to the team with the given ID
     */
    List<Shop> findByTeam_Id(Long teamId);
    
    /**
     * Find all stores by team ID and region
     * @param teamId The team ID
     * @param region The region name
     * @return List of stores belonging to the team with the given ID and in the specified region
     */
    List<Shop> findByTeam_IdAndDistrict(Long teamId, String region);
    
    /**
     * Custom query to find stores by team with additional filtering
     * @param teamId The team ID
     * @return List of stores belonging to the team with the given ID
     */
    @Query("SELECT s FROM Shop s WHERE s.team.id = :teamId AND s.active = true ORDER BY s.name")
    List<Shop> findActiveStoresByTeamId(@Param("teamId") Long teamId);
    
    @Query("SELECT s FROM Shop s WHERE s.latitude IS NULL OR s.longitude IS NULL")
    List<Shop> findByLatitudeIsNullOrLongitudeIsNull();
}