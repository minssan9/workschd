# Vercel 배포 가이드

이 문서는 workschd-vue3 프로젝트를 Vercel에 배포하는 방법을 설명합니다.

## 사전 준비

1. [Vercel 계정](https://vercel.com/) 생성
2. GitHub 리포지토리에 코드 푸시
3. 백엔드 API 서버 준비 (Spring Boot)

## 배포 단계

### 1. Vercel 프로젝트 생성

1. [Vercel Dashboard](https://vercel.com/dashboard)에 로그인
2. "Add New Project" 클릭
3. GitHub 리포지토리 연결
4. `workschd-vue3` 디렉토리 선택

### 2. 프로젝트 설정

**Framework Preset**: Vite
**Root Directory**: `workschd-vue3`
**Build Command**: `npm run build`
**Output Directory**: `dist`
**Install Command**: `npm install`

### 3. 환경 변수 설정

Vercel Dashboard > Settings > Environment Variables에서 다음 변수들을 추가하세요:

#### 필수 환경 변수

```bash
# API 서버 주소 (중요!)
VITE_API_BASE_URL=https://your-api-server.com

# Firebase 설정 (푸시 알림용)
VITE_FIREBASE_API_KEY=your_api_key
VITE_FIREBASE_AUTH_DOMAIN=your_project.firebaseapp.com
VITE_FIREBASE_PROJECT_ID=your_project_id
VITE_FIREBASE_STORAGE_BUCKET=your_project.appspot.com
VITE_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
VITE_FIREBASE_APP_ID=your_app_id
VITE_FIREBASE_MEASUREMENT_ID=your_measurement_id
VITE_FIREBASE_VAPID_KEY=your_vapid_key
```

#### 선택 환경 변수

```bash
# OAuth2 클라이언트 ID
VITE_GOOGLE_CLIENT_ID=your_google_client_id
VITE_KAKAO_CLIENT_ID=your_kakao_client_id
VITE_NAVER_CLIENT_ID=your_naver_client_id

# Channel.io 플러그인
VITE_CHANNEL_PLUGIN_KEY=your_channel_key
```

### 4. 배포 실행

1. "Deploy" 버튼 클릭
2. 빌드 로그 확인
3. 배포 완료 후 URL 확인

## CORS 설정 (백엔드)

Spring Boot 백엔드에서 Vercel 도메인을 허용해야 합니다:

```java
// SecurityConfig.java
@Configuration
public class SecurityConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "https://your-app.vercel.app",  // Vercel 프로덕션 URL
            "https://*.vercel.app",          // Vercel 프리뷰 URL
            "http://localhost:5173"          // 로컬 개발
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

## 자동 배포 설정

GitHub에 푸시할 때마다 자동으로 배포되도록 설정:

1. Vercel Dashboard > Settings > Git
2. "Production Branch" 설정: `main` 또는 `master`
3. "Automatic Deployments from Git" 활성화

## 커스텀 도메인 연결 (선택사항)

1. Vercel Dashboard > Settings > Domains
2. 도메인 입력 후 "Add" 클릭
3. DNS 레코드 설정:
   - Type: `A` 또는 `CNAME`
   - Value: Vercel이 제공하는 값

## 환경별 배포

### 프로덕션 (Production)
- Branch: `main` 또는 `master`
- URL: `https://your-app.vercel.app`

### 프리뷰 (Preview)
- Branch: `develop`, `feature/*` 등
- URL: `https://your-app-git-branch.vercel.app`

## 트러블슈팅

### 빌드 실패
```bash
# 로컬에서 빌드 테스트
npm run build

# TypeScript 에러 확인
npm run lint
```

### 404 에러 (새로고침 시)
- `vercel.json`의 `rewrites` 설정 확인
- SPA 라우팅을 위해 모든 경로를 `/index.html`로 리다이렉트

### API 연결 실패
- `VITE_API_BASE_URL` 환경 변수 확인
- 백엔드 CORS 설정 확인
- 브라우저 개발자 도구에서 네트워크 탭 확인

### 환경 변수 적용 안됨
- Vercel Dashboard에서 변수 확인
- 배포 후 재배포 (Deployments > ... > Redeploy)

## 모니터링

Vercel은 기본적으로 다음 메트릭을 제공합니다:

- **Analytics**: 페이지 뷰, 방문자 수
- **Speed Insights**: 성능 지표
- **Logs**: 빌드 및 함수 로그

## 참고 자료

- [Vercel 공식 문서](https://vercel.com/docs)
- [Vite 배포 가이드](https://vitejs.dev/guide/static-deploy.html)
- [Vue Router 히스토리 모드](https://router.vuejs.org/guide/essentials/history-mode.html)
