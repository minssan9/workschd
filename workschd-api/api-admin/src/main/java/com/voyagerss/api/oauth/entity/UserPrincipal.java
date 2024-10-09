package com.voyagerss.api.oauth.entity;

import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails, OidcUser {
    private final String userId;
    private final String password;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public Integer getUserId(){
        return Integer.parseInt(userId);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return userId;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    public static UserPrincipal create(Account account, List<AccountRole> accountRoleList) {
        Collection<GrantedAuthority> authorities =
                accountRoleList.stream()
                    .map(accountRole -> new SimpleGrantedAuthority(accountRole.getRoleType().getCode()))
                    .collect(Collectors.toList());

        return new UserPrincipal(
                account.getAccountId().toString(),
                account.getPassword(),
                authorities
        );
    }

    public static UserPrincipal create(Integer accountId, List<String> roles){

        Collection<GrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());

        return new UserPrincipal(
                accountId.toString(),
                "",
                authorities
        );
    }


    public static UserPrincipal create(Account account, Map<String, Object> attributes, List<AccountRole> accountRoleList) {
        UserPrincipal userPrincipal = create(account, accountRoleList);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }
}
