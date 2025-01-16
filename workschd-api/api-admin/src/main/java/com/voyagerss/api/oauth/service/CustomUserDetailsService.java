package com.voyagerss.api.oauth.service;

import com.voyagerss.api.oauth.entity.UserPrincipal;
import com.voyagerss.persist.entity.account.Account;
import com.voyagerss.persist.entity.account.AccountRole;
import com.voyagerss.persist.entity.account.AccountSns;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.AccountSnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final AccountSnsRepository accountSnsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountSns accountSns = accountSnsRepository.findFirstBySnsEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Can not find username."));
        Account account = accountSns.getAccount();
        List<AccountRole> accountRoleList = account.getAccountRoles();

        return UserPrincipal.create(account, accountRoleList);
    }
}


