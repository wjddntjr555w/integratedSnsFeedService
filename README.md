# SnsFeedService
정우석


---------------------------------------------
# 사용법


## 서버 실행
1. 코드 Download (ZIP파일)
2. 아래의 디렉토리 이동
#### SnsFeedService-master

3. 터미널 오픈 후 아래 명령어로 도커 down
#### docker-compose down
4. 아래 명령어로 도커 compose 실행 (mysql, spring boot server)
#### docker-compose up --build
5. 브라우저에서 아래의 링크로 접속
#### https://localhost:8080

## 기능
1. 자체 서비스 유저 회원가입 후 로그인 (SNS로그인 전 서비스 자체의 유저 로그인)
2. Facebook, Instagram 소셜 로그인 (Oauth를 통한 로그인)
   #### * 중요: Facebook, Instagram 둘 다 개발 중인 상태로 지정된 테스트 계정을 제외하고 개인정보 취득 권한이 없습니다.
   ####   (라이브 서비스를 위해서 사용자 정보 취급 이용 약관 작성이 필요, 정해진 테스트 계정으로 테스트해야 합니다.)
   #### * facebook : wjddntjr555w@naver.com , skttest12!@
   #### * instagram : wjddntjr555w@naver.com , skttest12!@
   
3. 통합 피드 보기 버튼 클릭
4. 기간 설정 후 조회 버튼 클릭
5. (스케쥴러를 통해 로그인 된 sns의 계정의 Feed를 60초마다 refresh)
