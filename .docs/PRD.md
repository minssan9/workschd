제품 요구사항 문서 (PRD): WorkSchd 근무 스케줄러 서비스

1. 프로젝트 개요

WorkSchd는 불규칙적인 근무 요일에 근무하는 매장의 관리자들을 위한 근무 스케줄러 서비스입니다. 이 서비스는 관리자들이 근태 계획을 수립하고, 출퇴근 시간을 관리하며, 인건비의 변동 폭을 예측할 수 있도록 도와줍니다. 최대한 단순한 UI를 제공하여 사용성이 높고, 핵심 기능에 집중한 MVP를 개발하는 것이 목표입니다.

2. 유저 플로우

    1.	회원가입 및 로그인
          •	관리자는 OAuth2를 통해 Google 또는 Kakao 계정으로 간편하게 로그인합니다.
    2.	매장 등록
          •	관리자는 자신이 관리하는 매장을 등록합니다.
    3.	직원 등록
          •	매장에 근무하는 직원들의 정보를 등록합니다.
    4.	근무 스케줄 생성
          •	직원들의 근무 가능 시간을 기반으로 근무 스케줄을 생성합니다.
    5.	근태 계획 수립
          •	생성된 스케줄을 검토하고 필요한 수정 사항을 반영합니다.
    6.	출퇴근 관리
          •	직원들의 실제 출퇴근 시간을 확인하고 기록합니다.
    7.	비용 분석
          •	예정된 스케줄과 실제 근무 시간에 따른 인건비의 차이를 확인합니다.

3. 핵심 기능

3.1 사용자 인증 및 권한 관리

	•	OAuth2 인증: Google과 Kakao 계정을 통한 로그인 기능.
	•	역할 기반 접근 제어: 관리자와 직원의 권한 구분.

3.2 매장 및 직원 관리

	•	매장 등록 및 관리: 여러 매장 등록 가능.
	•	직원 정보 관리: 직원의 근무 가능 시간, 연락처 등 정보 관리.

3.3 근무 스케줄링

	•	근무 스케줄 생성: 직원들의 근무 가능 시간을 기반으로 자동 스케줄 생성.
	•	스케줄 수정: 드래그 앤 드롭으로 스케줄 수정 가능.

3.4 출퇴근 관리

	•	출퇴근 기록: 직원들의 출퇴근 시간 기록 및 관리.
	•	실시간 업데이트: 직원들이 모바일을 통해 출퇴근 체크인/아웃.

<!-- 3.5 비용 분석

	•	인건비 계산: 예정된 근무 시간과 실제 근무 시간에 따른 인건비 계산.
	•	비용 차이 분석: 계획된 비용과 실제 비용의 차이 제공. -->

4. 기술 스택

프론트엔드
 
	•	Vue 3, Quasar
	•	상태 관리: Pinia 또는 Vuex 활용.

백엔드

	•	Spring Boot 3
	•	MySQL: 데이터베이스 관리.
	•	OAuth2 인증 서버: Spring Security OAuth2 클라이언트 설정.

DevOps

	•	CI/CD
	•	Docker 기반 컨테이너화.
	•	GitLab Actions: 자동 빌드 및 배포 파이프라인 설정.
	•	클라우드 인프라
	•	Digital Ocean: 서버 호스팅 및 관리.

추가 기술 스택

	•	API 통신
	•	RESTful API: 백엔드