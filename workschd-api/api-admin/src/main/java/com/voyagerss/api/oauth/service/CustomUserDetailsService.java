package com.voyagerss.api.oauth.service;

import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountRole;
import com.voyagerss.persist.entity.AccountSns;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.AccountSnsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyagerss.api.oauth.entity.UserPrincipal;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountSnsRepository accountSnsRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First try to find a regular account
        Account account = accountRepository.findByEmail(email)
                .orElseGet(() -> {
                    // If not found, try to find OAuth account
                    AccountSns accountSns = accountSnsRepository.findFirstBySnsEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
                    return accountSns.getAccount();
                });

        List<AccountRole> accountRoleList = account.getAccountRoles();

        return UserPrincipal.create(account, accountRoleList);
    }
}


