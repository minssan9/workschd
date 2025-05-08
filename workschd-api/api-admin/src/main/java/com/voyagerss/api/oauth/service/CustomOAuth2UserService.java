package com.voyagerss.api.oauth.service;

import com.voyagerss.api.oauth.entity.UserPrincipal;
import com.voyagerss.api.oauth.info.OAuth2UserInfo;
import com.voyagerss.api.oauth.info.OAuth2UserInfoFactory;
import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountRole;
import com.voyagerss.persist.entity.AccountSns;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.AccountRoleRepository;
import com.voyagerss.persist.repository.AccountSnsRepository;
import com.voyagerss.persist.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountService accountService;
    private final AccountRoleRepository accountRoleRepository;
    private final AccountSnsRepository accountSnsRepository;
    private final AccountRepository accountRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//        HashMap<String, String> attributes = new HashMap<>(oAuth2User.getAttribute(userNameAttributeName));
//         String oauth2Id = attributes.get("id");
//         String email = attributes.get("email");
//        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {

        EnumMaster.ProviderType providerType = EnumMaster.ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());

        Account savedAccount;
        AccountSns savedAccountSns = accountSnsRepository.findByProviderTypeAndSnsEmail(providerType, userInfo.getEmail());

        if (savedAccountSns != null) {
            savedAccount = savedAccountSns.getAccount();
            if (savedAccount != null) updateUser(savedAccount, userInfo);
        } else {
            savedAccount = createUser(userInfo, providerType);
        }
        List<AccountRole> savedAccountRoleList = accountRoleRepository.findByAccount_Email(userInfo.getEmail());
        return UserPrincipal.create(savedAccount, user.getAttributes(), savedAccountRoleList);
    }

    private Account createUser(OAuth2UserInfo userInfo, EnumMaster.ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        AccountDTO accountDTO = new AccountDTO(
                userInfo.getName(),
                userInfo.getPhone(),
                userInfo.getEmail(),
                userInfo.getImageUrl(),
                providerType,
                now,
                now
        );
        Account account = accountService.save(accountDTO);
        return account;
    }

    private Account updateUser(Account account, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !account.getUsername().equals(userInfo.getName())) {
            account.setUsername(userInfo.getName());
        }

        if (userInfo.getImageUrl() != null && account.getProfileImageUrl().isEmpty()) {
            account.setProfileImageUrl(userInfo.getImageUrl());
        }

        return account;
    }
}
