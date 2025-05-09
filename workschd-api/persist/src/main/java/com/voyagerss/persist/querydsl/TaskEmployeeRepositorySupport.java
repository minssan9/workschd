package com.voyagerss.persist.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.voyagerss.persist.dto.TaskEmployeeDTO;
import com.voyagerss.persist.entity.QAccount;
import com.voyagerss.persist.entity.QTask;
import com.voyagerss.persist.entity.QTaskEmployee;
import com.voyagerss.persist.entity.TaskEmployee;
import com.voyagerss.persist.repository.TaskEmployeeRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * QueryDSL support for TaskEmployee entity
 */
@Repository
public class TaskEmployeeRepositorySupport extends QuerydslRepositorySupport implements TaskEmployeeRepositoryCustom {

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public TaskEmployeeRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(TaskEmployee.class);
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    @PersistenceContext(unitName = "en9doorEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private final QTaskEmployee taskEmployee = QTaskEmployee.taskEmployee;
    private final QTask task = QTask.task;
    private final QAccount account = QAccount.account;
 
    /**
     * Find TaskEmployees using a DTO parameter for filtering
     * @param searchParams DTO containing search parameters
     * @param pageable Pagination information
     * @return Page of TaskEmployeeDTO
     */
    @Override
    public Page<TaskEmployeeDTO> findBySearchParams(TaskEmployeeDTO searchParams, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // Task-related filters
        if (searchParams.getTaskId() != null) {
            builder.and(task.id.eq(searchParams.getTaskId()));
        }
        
        if (StringUtils.hasText(searchParams.getTaskTitle())) {
            builder.and(task.title.containsIgnoreCase(searchParams.getTaskTitle()));
        }
        
        if (searchParams.getTaskStatus() != null) {
            builder.and(task.status.eq(searchParams.getTaskStatus()));
        }

        // Account-related filters
        if (searchParams.getAccountId() != null) {
            builder.and(account.accountId.eq(searchParams.getAccountId()));
        }
        
        if (StringUtils.hasText(searchParams.getAccountName())) {
            builder.and(account.username.containsIgnoreCase(searchParams.getAccountName()));
        }
        
        if (StringUtils.hasText(searchParams.getAccountEmail())) {
            builder.and(account.email.containsIgnoreCase(searchParams.getAccountEmail()));
        }

        // TaskEmployee-related filters
        if (searchParams.getId() != null) {
            builder.and(taskEmployee.id.eq(searchParams.getId()));
        }
        
        if (searchParams.getStatus() != null) {
            builder.and(taskEmployee.status.eq(searchParams.getStatus()));
        }
        
        if (searchParams.getRequestDate() != null) {
            builder.and(taskEmployee.requestDate.eq(searchParams.getRequestDate()));
        }
        
        if (searchParams.getApprovedAt() != null) {
            builder.and(taskEmployee.approvedAt.eq(searchParams.getApprovedAt()));
        }
        
        if (searchParams.getRejectedAt() != null) {
            builder.and(taskEmployee.rejectedAt.eq(searchParams.getRejectedAt()));
        }
        
        if (StringUtils.hasText(searchParams.getRejectionReason())) {
            builder.and(taskEmployee.rejectionReason.containsIgnoreCase(searchParams.getRejectionReason()));
        }
        
        if (searchParams.getJoinedAt() != null) {
            builder.and(taskEmployee.joinedAt.eq(searchParams.getJoinedAt()));
        }
        
        if (searchParams.getLeftAt() != null) {
            builder.and(taskEmployee.leftAt.eq(searchParams.getLeftAt()));
        }

        List<TaskEmployeeDTO> content = queryFactory
                .select(Projections.bean(TaskEmployeeDTO.class,
                        taskEmployee.id,
                        taskEmployee.task.id.as("taskId"),
                        task.title.as("taskTitle"),
                        task.status.as("taskStatus"),
                        taskEmployee.account.accountId,
                        account.username.as("accountName"),
                        account.email.as("accountEmail"),
                        taskEmployee.status,
                        taskEmployee.requestDate,
                        taskEmployee.approvedAt,
                        taskEmployee.rejectedAt,
                        taskEmployee.rejectionReason,
                        taskEmployee.joinedAt,
                        taskEmployee.leftAt
                ))
                .from(taskEmployee)
                .innerJoin(taskEmployee.task, task)
                .innerJoin(taskEmployee.account, account)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(taskEmployee.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(taskEmployee.count())
                .from(taskEmployee)
                .innerJoin(taskEmployee.task, task)
                .innerJoin(taskEmployee.account, account)
                .where(builder);

        return new PageImpl<>(content, pageable, countQuery.fetchOne());
    }
} 