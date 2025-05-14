아래 요구사항을 바탕으로 근무 스케줄러 서비스(workschd) 개발에 필요한 PRD(Product Requirements Document) 작성해줘:

- 불규칙적인 근무 요일에 근무하는 매장에서 근무자들의 관리자들이 근태 계획, 출퇴근 계획, 비용 차이가 얼마나 날지 알려주고,
- 최대한 단순한 UI
- 기능 구현에 필요한 라이브러리는 최대한 활용
- MVP(Minimum Viable Product) 개발에 집중. 복잡한 기능보다는 핵심 기능 완성도에 초점
- 필수 기술스택
  - Next.js App Router, 
  - Vue3, TailwindCSS grid 기반 레이아웃
  - Spring boot 3
  - mysql
  - 인증 : OAuth2 google, kakao
  - CI, CD : docker base, gitlab actions
  - Cloud : Digital Ocean
- 요구사항에 따른 추가적인 기술스택 업데이트


PRD는 다음 내용을 포함해야해:

-   프로젝트 개요
-   유저 플로우
-   핵심 기능
-   기술스택
-   MVP 기능 개발 이후 추가 개선사항
 

---
ShadCN,

1. Team 관리
    1. Team 멤버 관리
        1. Team 생성 (매니저)
        2. Team 입장 요청(멤버)
        3. 이름, 위치, 선호 작업장 등록
        4. Team 입장 허가(매니저)
2. Team 작업장 관리
    1. 작업장 등록
3. Team 업무 관리
    1. 오늘의 업무 등록
    2. 참여 신청(멤버)
    3. 참여 승인(매니저)
    4. 일정 공유