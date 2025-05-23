package com.voyagerss.api.oauth.handler;

import com.voyagerss.api.component.properties.AuthProperties;
import com.voyagerss.api.component.util.CookieUtil;
import com.voyagerss.api.oauth.info.OAuth2UserInfo;
import com.voyagerss.api.oauth.info.OAuth2UserInfoFactory;
import com.voyagerss.api.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.voyagerss.api.oauth.token.JwtTokenProvider;
import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountSns;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.AccountRoleRepository;
import com.voyagerss.persist.repository.AccountSnsRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.voyagerss.api.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.voyagerss.api.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

@Component
@Transactional
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired    private AccountRepository accountRepository;
    @Autowired    private AccountRoleRepository accountRoleRepository;
    @Autowired    private AccountSnsRepository accountSnsRepository;
    private final AuthProperties authProperties;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        EnumMaster.ProviderType providerType = EnumMaster.ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();

        AccountSns accountSns = accountSnsRepository.findByProviderTypeAndSnsEmail(providerType, userInfo.getEmail());
        Account account = accountSns.getAccount();
        if(account == null ) return "redirect:/login/fail";

        List<String> roleList = account.getAccountRoles().stream()
                .map(accountRole -> accountRole.getRoleType().getCode())
                .collect(Collectors.toList());

        String accessToken = jwtTokenProvider.createAccessToken(account.getAccountId(), userInfo.getEmail(),roleList);
        String refreshToken = jwtTokenProvider.createRefreshToken(account.getAccountId(), userInfo.getEmail(),roleList);

        account.setAccessToken(accessToken);
        account.setRefreshToken(refreshToken);
        accountRepository.save(account);

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken, 30 * 60 * 1000);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return authProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
