package com.voyagerss.api.oauth.filter;

import com.voyagerss.api.oauth.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${token_server}")
    private String AUTH_TOKEN;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Skip authentication for static resources
        String path = request.getRequestURI();
        if (shouldSkipAuthentication(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        // 유효한 토큰인지 확인합니다.
        if (accessToken != null) {
            // 어세스 토큰이 유효한 상황
            try {
                if (jwtTokenProvider.validateToken(accessToken)) {
                    this.setAuthentication(accessToken);
                }
                // 어세스 토큰이 만료된 상황 | 리프레시 토큰 또한 존재하는 상황
                else if (!jwtTokenProvider.validateToken(accessToken) && refreshToken != null) {
                    // 재발급 후, 컨텍스트에 다시 넣기
                    /// 리프레시 토큰 검증
                    boolean validateRefreshToken = jwtTokenProvider.validateToken(refreshToken);
                    /// 리프레시 토큰 저장소 존재유무 확인
                    boolean isRefreshToken = jwtTokenProvider.existsRefreshToken(refreshToken);
                    if (validateRefreshToken && isRefreshToken) {
                        /// 리프레시 토큰으로 Account Id 정보 가져오기
                        Long accountId = jwtTokenProvider.getUserId(refreshToken);
                        /// 리프레시 토큰으로 이메일 정보 가져오기
                        String email = jwtTokenProvider.getUserEmail(refreshToken);
                        /// 이메일로 권한정보 받아오기
                        List<String> roles = jwtTokenProvider.getRoles(refreshToken);

                        /// 토큰 발급
                        String newAccessToken = jwtTokenProvider.createAccessToken(accountId, email, roles);
                        /// 헤더에 어세스 토큰 추가
                        jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
                        /// 컨텍스트에 넣기
                        accessToken = newAccessToken;
                    }
                }
            } catch (SecurityException e) {
                log.info("Invalid JWT signature.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (MalformedJwtException e) {
                log.info("Invalid JWT token.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (ExpiredJwtException e) {
                log.info("Expired JWT token.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (UnsupportedJwtException e) {
                log.info("Unsupported JWT token.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IllegalArgumentException e) {
                log.info("JWT token compact of handler are invalid.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipAuthentication(String path) {
        return path.contains("/static/") || 
               path.contains("/assets/") ||
               path.contains(".ico") ||
               path.contains(".js") ||
               path.contains(".css") ||
               path.contains(".png") ||
               path.contains(".jpg") ||
               path.contains(".gif");
    }

    // SecurityContext 에 Authentication 객체를 저장합니다.
    public void setAuthentication(String token) {
        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
