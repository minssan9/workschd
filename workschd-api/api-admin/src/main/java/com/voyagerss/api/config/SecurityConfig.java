package com.voyagerss.api.config;

import com.voyagerss.api.component.filter.CustomTokenFilter;
import com.voyagerss.api.component.properties.AppProperties;
import com.voyagerss.api.oauth.exception.RestAuthenticationEntryPoint;
import com.voyagerss.api.oauth.filter.TokenAuthenticationFilter;
import com.voyagerss.api.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.voyagerss.api.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.voyagerss.api.oauth.handler.TokenAccessDeniedHandler;
import com.voyagerss.api.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.voyagerss.api.oauth.service.CustomOAuth2UserService;
import com.voyagerss.api.oauth.service.CustomUserDetailsService;
import com.voyagerss.api.oauth.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppProperties appProperties;

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomTokenFilter customTokenFilter;

    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;

    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션을 사용하지 않음
                )
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화
                .formLogin(AbstractHttpConfigurer::disable)  // Form-based 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP Basic 인증 비활성화
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()  // CORS Pre-flight 요청 허용
                        .requestMatchers("/v3/api-docs",
                                "/configuration/ui",
                                "/swagger-resources/**",
                                "/configuration/security",
                                "/swagger-ui.html").permitAll()
                        .requestMatchers("/health").permitAll()  // 특정 경로 허용
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/common/**").permitAll()
                        .requestMatchers("/ed/common/**").permitAll()
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization")  // OAuth2 인증 엔드포인트 설정
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())  // Authorization 요청 저장소 설정
                        )
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/*/oauth2/code/*")  // 리다이렉션 엔드포인트 설정
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)  // 사용자 서비스 설정
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler())  // 성공 핸들러 설정
                        .failureHandler(oAuth2AuthenticationFailureHandler())  // 실패 핸들러 설정
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(tokenAccessDeniedHandler)
                );

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(customTokenFilter, TokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }
    /*
    * auth 매니저 설정
    * */
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }

    /*
    * security 설정 시, 사용할 인코더 설정
    * */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    * 토큰 필터 설정
    * */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(jwtTokenProvider);
    }

    /*
    * 쿠키 기반 인가 Repository
    * 인가 응답을 연계 하고 검증할 때 사용.
    * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
    * Oauth 인증 성공 핸들러
    * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                appProperties,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /*
    * Cors 설정
    * */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(appProperties.getCors().getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(appProperties.getCors().getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(appProperties.getCors().getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }


}
