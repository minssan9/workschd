package com.voyagerss.api.oauth.token;

import com.voyagerss.api.component.properties.AppProperties;
import com.voyagerss.api.oauth.entity.UserPrincipal;
import com.voyagerss.api.oauth.service.CustomUserDetailsService;
import com.voyagerss.persist.repository.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt_secret}")
    private String secret;
    private Key secretKey;

    private long accessTokenValidTime = 6 * 60 * 60 * 1000L; // 6h * 60m * 60s * 1000L;
    private long refreshTokenValidTime = 24 * 60 * 60 * 1000L; // 24h * 60 * 60 * 1000L;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String HEADER_REFRESH_TOKEN = "refreshToken";
    private final static String TOKEN_PREFIX = "Bearer ";

    private final AppProperties appProperties;
    private final CustomUserDetailsService customUserDetailsService;
    private final AccountRepository accountRepository;

    // 의존성 주입 후, 초기화를 수행
    // 객체 초기화, secret를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        accessTokenValidTime = appProperties.getAuth().getTokenExpiry() * 60 * 1000L ;
        refreshTokenValidTime = appProperties.getAuth().getRefreshTokenExpiry() * 60 * 1000L ;
    }

    // Access Token 생성.
    public String createAccessToken(Integer accountId, String email, List<String> roles){
        return this.createToken(accountId, email, roles, accessTokenValidTime);
    }
    // Refresh Token 생성.
    public String createRefreshToken(Integer accountId, String email, List<String> roles) {
        return this.createToken(accountId, email, roles, refreshTokenValidTime);
    }

    // Create token
    public String createToken(Integer accountId, String email, List<String> roles, long tokenValid) {
        Claims claims = Jwts.claims();
        claims.setSubject(email); // claims 생성 및 payload 설정
        claims.put("accountId", accountId);
        claims.put("roles", roles); // 권한 설정, key/ value 쌍으로 저장

        Date date = new Date();

        return Jwts.builder()
                .setSubject(email)
                .setClaims(claims) // 발행 유저 정보 저장
//                .setIssuedAt(date) // 발행 시간 저장
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 해싱 알고리즘 및 키 설정
                .setExpiration(new Date(date.getTime() + tokenValid))  // 토큰 유효 시간 저장
                .compact();
    }


    // Create token
    public String createToken(Authentication authentication) {
        log.info(authentication.toString());
        Claims claims = Jwts.claims();
//        claims.setSubject(email); // claims 생성 및 payload 설정
//        claims.put("accountId", accountId);
//        claims.put("roles", roles); // 권한 설정, key/ value 쌍으로 저장

        Date date = new Date();

        return Jwts.builder()
//                .setSubject(email)
                .setClaims(claims) // 발행 유저 정보 저장
//                .setIssuedAt(date) // 발행 시간 저장
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 해싱 알고리즘 및 키 설정
//                .setExpiration(new Date(date.getTime() + tokenValid))  // 토큰 유효 시간 저장
                .compact();
    }

    // JWT 에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getUserEmail(token));
        UserPrincipal.create(getUserId(token), getRoles(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    // 토큰에서 회원 정보 추출
    public Integer getUserId(String token) {
        return Integer.parseInt(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("accountId").toString());
    }

    // 토큰에서 회원 정보 추출
    public String getUserEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Token 권한 정보 가져오기
    public List<String> getRoles(String token) {
        return (List<String>) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles");
    }

    // Request의 Header에서 AccessToken 값을 가져옵니다. "authorization" : "token'
    public String resolveAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);
        if (headerValue == null) return null;
        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }


    // Request의 Header에서 RefreshToken 값을 가져옵니다. "authorization" : "token'
    public String resolveRefreshToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_REFRESH_TOKEN);

        if (headerValue == null) return null;
        return headerValue;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String token)   {
        Claims claims = null;
        claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().after(new Date());
    }

    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("authorization", "bearer "+ accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "bearer "+ refreshToken);
    }

    // RefreshToken 존재유무 확인
    public boolean existsRefreshToken(String refreshToken) {
        return accountRepository.existsByRefreshToken(refreshToken);
    }




//    // Email로 권한 정보 가져오기
//    public List<String> getRoles(String email) {
//        List<String> roleList = accountRoleRepository.findByAccount_Email(email)
//                .stream()
//                .map(accountRole -> accountRole.getRoleType().getCode())
//                .collect(Collectors.toList());
//        return roleList;
//    }
}
