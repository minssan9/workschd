# Digital Ocean Deployment Guide

이 문서는 Workschd 애플리케이션을 Digital Ocean Droplet에 배포하는 방법을 설명합니다.

## 목차

- [아키텍처](#아키텍처)
- [사전 준비](#사전-준비)
- [Droplet 초기 설정](#droplet-초기-설정)
- [로컬 환경 설정](#로컬-환경-설정)
- [배포 실행](#배포-실행)
- [SSL 인증서 설정](#ssl-인증서-설정)
- [모니터링 및 로그](#모니터링-및-로그)
- [트러블슈팅](#트러블슈팅)

---

## 아키텍처

```
Internet
    |
    v
[Nginx Gateway :80, :443]
    |
    +---> [Vue3 Frontend Container :80]
    |
    +---> [Spring Boot Backend :8080]
              |
              v
          [MariaDB :3306]
```

### 컨테이너 구성

- **nginx**: 리버스 프록시 및 게이트웨이 (포트 80, 443)
- **frontend**: Vue3 + Quasar 프론트엔드 (내부 포트 80)
- **backend**: Spring Boot API 서버 (내부 포트 8080)
- **db**: MariaDB 데이터베이스 (포트 3306)

---

## 사전 준비

### 1. Digital Ocean 계정 및 Droplet 생성

1. [Digital Ocean](https://www.digitalocean.com/) 계정 생성
2. Droplet 생성:
   - **이미지**: Ubuntu 22.04 LTS
   - **Plan**: Basic ($12/mo 권장, 2GB RAM)
   - **Region**: 사용자와 가까운 지역 선택
   - **Authentication**: Password (root 비밀번호 설정)

3. Droplet IP 주소와 root 비밀번호 확인

### 2. 로컬 환경 요구사항

- Node.js 18+
- Java 17+
- Docker (테스트용)
- Git
- **sshpass** (SSH 패스워드 인증용)
  - macOS: `brew install hudochenkov/sshpass/sshpass`
  - Linux: `sudo apt-get install sshpass`

---

## Droplet 초기 설정

### 1. Droplet 접속

```bash
ssh root@your.droplet.ip.address
```

### 2. 자동 설정 스크립트 실행

로컬에서 설정 스크립트를 Droplet에 업로드:

```bash
sshpass -p 'your_password' scp -o StrictHostKeyChecking=no scripts/setup-droplet.sh root@your.droplet.ip.address:/tmp/
```

Droplet에서 스크립트 실행:

```bash
sshpass -p 'your_password' ssh -o StrictHostKeyChecking=no root@your.droplet.ip.address
cd /tmp
chmod +x setup-droplet.sh
./setup-droplet.sh
```

> **참고**: root 사용자로 직접 배포하므로 별도 deploy 사용자 생성은 불필요합니다.

### 3. 수동 설정 (선택사항)

자동 스크립트 대신 수동으로 설정하려면:

```bash
# Docker 설치
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# Docker Compose 설치
DOCKER_COMPOSE_VERSION="v2.24.0"
curl -L "https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# 배포 사용자 생성
useradd -m -s /bin/bash deploy
usermod -aG docker deploy
mkdir -p /home/deploy/workschd
chown -R deploy:deploy /home/deploy/workschd

# 방화벽 설정
ufw allow 22/tcp
ufw allow 80/tcp
ufw allow 443/tcp
ufw --force enable
```

### 4. SSH 키 인증 설정 (deploy 사용자)

```bash
# root로 실행
su - deploy
mkdir -p ~/.ssh
chmod 700 ~/.ssh

# 로컬의 공개 키를 추가
echo "your-public-key-content" >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```

---

## 로컬 환경 설정

### 1. 환경 변수 파일 생성

`.env.production.example`을 복사하여 `.env.production` 생성:

```bash
cp .env.production.example .env.production
```

`.env.production` 파일 수정:

```bash
# Digital Ocean Droplet Configuration
DROPLET_IP=123.456.789.012          # Droplet IP 주소
DROPLET_USER=root
DROPLET_PASSWORD=your_droplet_password  # Droplet root 비밀번호
DEPLOY_PATH=/root/workschd

# Database Configuration
DB_ROOT_PASSWORD=strong_root_password_here
DB_PASSWORD=strong_db_password_here

# Application Configuration
SPRING_PROFILES_ACTIVE=prod

# Domain (선택사항)
DOMAIN=your-domain.com
```

**⚠️ 보안 주의사항**:
- `.env.production` 파일은 절대 Git에 커밋하지 마세요
- 비밀번호는 강력하게 설정하세요 (최소 16자, 영문+숫자+특수문자)
- 배포 후 Droplet의 SSH 포트를 변경하는 것을 권장합니다

### 2. 애플리케이션 환경 변수 설정

백엔드 설정 (`workschd-api/src/main/resources/application-prod.yml`):

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DATABASE_HOST:db}:${DATABASE_PORT:3306}/${DATABASE_NAME:workschd}
    username: ${DATABASE_USER:workschd_user}
    password: ${DATABASE_PASSWORD}

server:
  port: 8080
```

프론트엔드 환경 변수 (`workschd-vue3/.env.production`):

```bash
VITE_API_BASE_URL=/api
```

---

## 배포 실행

### 1. 첫 배포

```bash
./scripts/deploy.sh production
```

배포 스크립트는 다음 작업을 수행합니다:

1. ✅ Vue3 프론트엔드 빌드
2. ✅ Spring Boot 백엔드 빌드
3. ✅ 배포 패키지 생성 (tar.gz)
4. ✅ Droplet에 업로드
5. ✅ Docker 컨테이너 빌드 및 실행
6. ✅ 헬스 체크

### 2. 배포 진행 상황 확인

배포 중 출력되는 로그를 확인하세요:

```
[1/6] Building Vue3 frontend...
[2/6] Building Spring Boot backend...
[3/6] Creating deployment package...
[4/6] Uploading to droplet...
[5/6] Extracting and preparing on droplet...
[6/6] Deploying with Docker Compose...
```

### 3. 배포 후 확인

브라우저에서 접속:

```
http://your.droplet.ip.address
```

API 헬스 체크:

```bash
curl http://your.droplet.ip.address/api/health
```

### 4. 컨테이너 상태 확인

Droplet에서:

```bash
sshpass -p 'your_password' ssh root@your.droplet.ip.address
cd /root/workschd
docker-compose ps
```

---

## SSL 인증서 설정

### 1. 도메인 연결

Digital Ocean에서 도메인 DNS 설정:

```
A Record: @ -> your.droplet.ip.address
A Record: www -> your.droplet.ip.address
```

### 2. Let's Encrypt 인증서 발급

Droplet에서:

```bash
# Certbot으로 SSL 인증서 발급
sudo certbot certonly --standalone -d your-domain.com -d www.your-domain.com

# 인증서 파일 복사
sudo cp /etc/letsencrypt/live/your-domain.com/fullchain.pem /root/workschd/nginx/ssl/
sudo cp /etc/letsencrypt/live/your-domain.com/privkey.pem /root/workschd/nginx/ssl/
```

### 3. Nginx 설정 업데이트

`nginx/conf.d/default.conf`에서 HTTPS 섹션 주석 해제:

```nginx
server {
    listen 443 ssl http2;
    server_name your-domain.com;

    ssl_certificate /etc/nginx/ssl/fullchain.pem;
    ssl_certificate_key /etc/nginx/ssl/privkey.pem;

    # ... (나머지 설정)
}
```

### 4. 컨테이너 재시작

```bash
cd /root/workschd
docker-compose restart nginx
```

### 5. 자동 갱신 설정

```bash
# Crontab 편집
sudo crontab -e

# 매월 1일 새벽 3시에 갱신
0 3 1 * * certbot renew --quiet && cp /etc/letsencrypt/live/your-domain.com/*.pem /root/workschd/nginx/ssl/ && cd /root/workschd && docker-compose restart nginx
```

---

## 모니터링 및 로그

### 1. 컨테이너 로그 확인

```bash
# 모든 컨테이너 로그
docker-compose logs -f

# 특정 컨테이너 로그
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f nginx

# 최근 100줄만 확인
docker-compose logs --tail=100 backend
```

### 2. 시스템 리소스 모니터링

```bash
# Docker 컨테이너 리소스 사용량
docker stats

# 시스템 리소스
htop
```

### 3. 로그 파일 위치

```
/home/deploy/workschd/
├── logs/
│   ├── nginx/
│   │   ├── access.log
│   │   └── error.log
│   └── backend/
│       └── application.log
```

### 4. 데이터베이스 접속

```bash
# MariaDB 컨테이너 접속
docker exec -it workschd-db mysql -u workschd_user -p

# 또는 외부에서
mysql -h your.droplet.ip.address -u workschd_user -p workschd
```

---

## 업데이트 및 재배포

### 1. 코드 업데이트 후 재배포

```bash
# Git에서 최신 코드 받기
git pull origin main

# 재배포
./scripts/deploy.sh production
```

### 2. 롤백

문제가 발생한 경우 이전 버전으로 롤백:

```bash
./scripts/rollback.sh production
```

### 3. 특정 컨테이너만 재시작

```bash
sshpass -p 'your_password' ssh root@your.droplet.ip.address
cd /root/workschd

# 백엔드만 재시작
docker-compose restart backend

# 프론트엔드만 재시작
docker-compose restart frontend
```

---

## 트러블슈팅

### 1. 배포 스크립트 실패

**문제**: SSH 연결 실패

```bash
# sshpass 설치 확인
which sshpass

# sshpass 설치 (macOS)
brew install hudochenkov/sshpass/sshpass

# sshpass 설치 (Linux)
sudo apt-get install sshpass

# SSH 연결 테스트
sshpass -p 'your_password' ssh -o StrictHostKeyChecking=no root@your.droplet.ip.address
```

**문제**: 빌드 실패

```bash
# 로컬에서 빌드 테스트
cd workschd-vue3
npm run build

cd ../workschd-api
./gradlew clean build
```

### 2. 컨테이너 시작 실패

```bash
# 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs backend

# 컨테이너 재시작
docker-compose restart
```

### 3. 데이터베이스 연결 실패

```bash
# DB 컨테이너 상태 확인
docker-compose logs db

# DB 컨테이너 재시작
docker-compose restart db

# 연결 테스트
docker exec -it workschd-db mysql -u workschd_user -p
```

### 4. Nginx 502 Bad Gateway

원인: 백엔드가 아직 시작되지 않음

```bash
# 백엔드 로그 확인
docker-compose logs backend

# 백엔드 재시작
docker-compose restart backend

# 30초 대기 후 다시 접속
```

### 5. 포트 충돌

```bash
# 포트 사용 확인
sudo netstat -tulpn | grep :80
sudo netstat -tulpn | grep :443

# Nginx 서비스 중지 (시스템 Nginx와 충돌 시)
sudo systemctl stop nginx
```

### 6. 디스크 공간 부족

```bash
# 디스크 사용량 확인
df -h

# Docker 정리
docker system prune -a

# 오래된 로그 삭제
find /root/workschd/logs -name "*.log" -mtime +30 -delete
```

---

## 백업 및 복구

### 1. 데이터베이스 백업

```bash
# 백업 스크립트
docker exec workschd-db mysqldump -u workschd_user -p workschd > backup_$(date +%Y%m%d).sql
```

### 2. 자동 백업 설정

```bash
# Crontab 편집
crontab -e

# 매일 새벽 2시에 백업
0 2 * * * cd /root/workschd && docker exec workschd-db mysqldump -u workschd_user -pYOUR_PASSWORD workschd > backup_$(date +\%Y\%m\%d).sql
```

### 3. 복구

```bash
# 백업 파일 복구
docker exec -i workschd-db mysql -u workschd_user -p workschd < backup_20250115.sql
```

---

## 성능 최적화

### 1. Docker 리소스 제한

`docker-compose.yml` 수정:

```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 512M
```

### 2. Nginx 캐싱 설정

이미 `nginx/conf.d/default.conf`에 캐싱 설정이 포함되어 있습니다.

### 3. 데이터베이스 최적화

MariaDB 설정 튜닝 (고급):

```bash
# my.cnf 파일 생성
docker exec -it workschd-db bash
vi /etc/mysql/my.cnf
```

---

## 보안 권장사항

1. **방화벽 설정 확인**
   ```bash
   sudo ufw status
   ```

2. **SSH 보안 강화**
   ```bash
   # /etc/ssh/sshd_config 편집
   PermitRootLogin yes  # 필요시 no로 변경하고 일반 사용자 사용
   PasswordAuthentication yes  # SSH 키로 전환 시 no
   ```

3. **비밀번호 정기 변경**
   - Droplet root 비밀번호
   - DB 비밀번호
   - 환경 변수 비밀번호

4. **정기 업데이트**
   ```bash
   sudo apt update && sudo apt upgrade -y
   ```

5. **sshpass 대안 (더 안전)**
   - SSH 키 방식으로 전환 권장
   - `expect` 스크립트 사용
   - Ansible 등 배포 자동화 도구 사용

---

## 참고 자료

- [Docker 공식 문서](https://docs.docker.com/)
- [Docker Compose 공식 문서](https://docs.docker.com/compose/)
- [Digital Ocean 가이드](https://www.digitalocean.com/community/tutorials)
- [Let's Encrypt 문서](https://letsencrypt.org/docs/)
- [Nginx 공식 문서](https://nginx.org/en/docs/)
