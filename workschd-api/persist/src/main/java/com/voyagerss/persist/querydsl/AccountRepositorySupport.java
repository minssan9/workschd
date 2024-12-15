package com.voyagerss.persist.querydsl;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.component.util.QueryDslUtil;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.dto.QueryDTO;
import com.voyagerss.persist.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class AccountRepositorySupport extends QuerydslRepositorySupport {
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public AccountRepositorySupport(JPAQueryFactory mariaJpaQueryFactory) {
        super(Account.class);
        this.queryFactory = mariaJpaQueryFactory;
    }

    @Override
    @PersistenceContext(unitName = "en9doorEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    QAccount account = QAccount.account;
    QAccountRole accountRole = QAccountRole.accountRole;
    QAccountSns accountSns = QAccountSns.accountSns;
    QAccountInfo accountInfo = QAccountInfo.accountInfo;

    private JPAQuery getDtoQuery(QueryDTO queryDTO){
        JPAQuery query = queryFactory.select(Projections.fields(AccountDTO.class,
                        account.accountId,
                        account.username,
                        account.email,
                        account.phone,
                        account.createdAt,
                        accountRole.roleType,
                        account.profileImageUrl,
                        account.profileVideoUrl,
                        accountSns.accessToken,
                        accountSns.providerType,
                        accountSns.snsEmail,
                        accountSns.profileImageUrl,
                        account.username,
                        account.password,
                        account.email,
                        account.englishName,
                        account.phone,
                        account.profileImageUrl,
                        account.profileVideoUrl,
                        account.incentivePercent,
                        accountRole,
                        accountSns,
                        accountRole.roleType,
                        accountSns.userId
                    ))
                .distinct()
                .from(account)
                .innerJoin(accountSns).on(account.eq(accountSns.account)).fetchJoin()
                .leftJoin(accountRole).on(account.eq(accountRole.account)).fetchJoin()
                .leftJoin(accountInfo).on(account.eq(accountInfo.account)).fetchJoin()
                .where(
                        eqAccountId(queryDTO.getAccountId()),
                        containsRoleType(queryDTO.getRole()),
                        likeAccountName(queryDTO.getUsername()),
                        likeEmail(queryDTO.getEmail()),
                        likePhone(queryDTO.getPhone())
                )
                .orderBy(account.createdAt.desc());
        return query;
    }
    private JPAQuery getQuery(QueryDTO queryDTO){

        JPAQuery query = queryFactory.selectFrom(account)
                .distinct()
                .innerJoin(accountSns).on(account.eq(accountSns.account)).fetchJoin()
                .leftJoin(accountRole).on(account.eq(accountRole.account)).fetchJoin()
                .leftJoin(accountInfo).on(accountInfo.eq(account.accountInfo)).fetchJoin()
                .where(
                        eqAccountId(queryDTO.getAccountId()),
                        containsRoleType(queryDTO.getRole()),
                        likeAccountName(queryDTO.getUsername()),
                        likeEmail(queryDTO.getEmail()),
                        likePhone(queryDTO.getPhone())
                )
                .orderBy(account.createdAt.desc());
        return query;
    }

    public AccountDTO getAccountDtoOne(QueryDTO queryDTO) {
        JPAQuery query = getDtoQuery(queryDTO);

        AccountDTO account = (AccountDTO) query.fetchOne();
        return account;
    }

    public Page<AccountDTO> getAccountDtoPage(QueryDTO queryDTO) {
        JPAQuery query = getQuery(queryDTO);

        List<Account> accounts = getQuerydsl().applyPagination(queryDTO.getPageable(), query).fetch();
        return new PageImpl(accounts.stream().map(Account::toDto).toList(), queryDTO.getPageable(), query.fetchCount());
    }

    public List<AccountDTO> getAccountDtoList(QueryDTO queryDTO) {
        JPAQuery query = getQuery(queryDTO);

        List<Account> accounts = query.fetch();
        return accounts.stream().map(Account::toDto).toList();
    }

    public Account getAccountOne(QueryDTO queryDTO) {
        JPAQuery query = getQuery(queryDTO);

        Account account = (Account) query.fetchOne();
        return account;
    }

//    public Page<Account> getAccountPage(QueryDTO queryDTO) {
//        JPAQuery query = getQuery(queryDTO);
//
//        List<Account> accounts = getQuerydsl().applyPagination(queryDTO.getPageable(), query).fetch();
//        return new PageImpl(accounts, queryDTO.getPageable(), query.fetchCount());
//    }

//    public List<Account> getAccountList(QueryDTO queryDTO) {
//        JPAQuery query = getQuery(queryDTO);
//
//        List<Account> accounts = query.fetch();
//        return accounts;
//    }

    private BooleanExpression eqAccountId(Integer accountId) {
        if (accountId == null) return null;
        return account.accountId.eq(accountId);
    }
    private BooleanExpression likeAccountName(String accountName) {
        if (accountName == null) return null;
        return account.username.like("%" + accountName + "%");
    }

    private BooleanExpression likePhone(String phone) {
        if (phone == null) return null;
        return account.phone.like("%" + phone + "%");
    }

    private BooleanExpression likeEmail(String email) {
        if (email == null) return null;
        return account.email.like("%" + email + "%");
    }

    private BooleanExpression containsRoleType(EnumMaster.RoleType roleType) {
        if (roleType == null) return null;
        return accountRole.roleType.eq(roleType);
    }

    private BooleanExpression eqAccessToken(String accessToken) {
        if (accessToken == null) return null;
        return accountSns.accessToken.eq(accessToken);
    }

    private BooleanExpression eqStatus(Integer teacherId) {
        if (teacherId == null) return null;
        return account.accountId.eq(teacherId);
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "created_at": OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(direction, account, "createdAt");
                        ORDERS.add(orderId);
                        break;
                    case "username": OrderSpecifier<?> orderUser = QueryDslUtil.getSortedColumn(direction, account, "username");
                        ORDERS.add(orderUser);
                        break;
                    default:
                        break;
                }
            }
        }
        return ORDERS;
    }
}
