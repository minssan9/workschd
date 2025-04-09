package com.voyagerss.persist.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.voyagerss.persist.dto.TeamDTO;
import com.voyagerss.persist.entity.QTeam;
import com.voyagerss.persist.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamRepositorySupport extends QuerydslRepositorySupport {
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public TeamRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Team.class);
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    @PersistenceContext(unitName = "en9doorEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    QTeam team = QTeam.team;

    private JPAQuery<TeamDTO> getDtoQuery(TeamDTO teamDTO) {
        JPAQuery<TeamDTO> query = queryFactory.select(Projections.fields(TeamDTO.class,
                        team.id,
                        team.name,
                        team.region,
                        team.scheduleType,
                        team.invitationHash,
                        team.invitationCreatedAt,
                        team.location,
                        team.createdAt,
                        team.lastModifiedAt
                ))
                .from(team)
                .where(
                        eqTeamId(teamDTO.getId())       ,
                        likeTeamName(teamDTO.getName()),
                        eqInvitationHash(teamDTO.getInvitationHash())
                )
                .orderBy(team.createdAt.desc());
        return query;
    }

    private JPAQuery<Team> getQuery(TeamDTO teamDTO) {
        JPAQuery<Team> query = queryFactory.selectFrom(team)
                .where(
                        eqTeamId(teamDTO.getId())       ,
                        likeTeamName(teamDTO.getName()),
                        eqInvitationHash(teamDTO.getInvitationHash())
                )
                .orderBy(team.createdAt.desc());
        return query;
    }

    public Page<TeamDTO> getTeamDtoPage(TeamDTO teamDTO) {
        JPAQuery<TeamDTO> query = getDtoQuery(teamDTO);

        List<TeamDTO> teams = getQuerydsl().applyPagination(teamDTO.getPageable(), query).fetch();
        return new PageImpl<>(teams, teamDTO.getPageable(), query.fetchCount());
    }

    public List<TeamDTO> getTeamDtoList(TeamDTO teamDTO) {
        JPAQuery<TeamDTO> query = getDtoQuery(teamDTO);
        return query.fetch();
    }

    public TeamDTO getTeamDtoOne(TeamDTO teamDTO) {
        JPAQuery<TeamDTO> query = getDtoQuery(teamDTO);
        return query.fetchOne();
    }

    private BooleanExpression eqTeamId(Long id) {
        if (id == null) return null;
        return team.id.eq(id);
    }

    private BooleanExpression likeTeamName(String name) {
        if (name == null) return null;
        return team.name.like("%" + name + "%");
    }

    private BooleanExpression eqInvitationHash(String invitationHash) {
        if (invitationHash == null) return null;
        return team.invitationHash.eq(invitationHash);
    }
} 