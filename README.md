## Bingterpark
[노션 페이지](https://www.notion.so/backend-devcourse/2-BingterPark-4ecfb3943d9c4a8f9bb83f72876b6a80)  
[ERD](https://www.erdcloud.com/d/ZadArGCaQXFcxZuu8)
### 모듈 구조
#### api
- api-member   
회원 도메인
- api-event   
공연 도메인
- api-booking   
결제 도메인
#### batch
스프링 배치 모듈
#### core
- core-domain   
JPA 엔티티, 리포지토리 
- core-infra   
queryDsl, RDB 설정 파일
- core-infra-es   
elastic search 설정 파일, document, searchRepository
- core-security   
spring security 설정 파일
## 실행 방법
1. git clone
2. RDB, 레디스 실행 ```docker-compose up -d```
3. api-event 모듈로 이동 ```cd /api/api-event```
4. 엘라스틱 서치 도커 이미지 빌드 ```docker build -t el:0.1 -f ./Dockerfile .```
5. ELK 스택 실행 ```docker-compose up -d```
6. api-booking, api-event, api-member 각 모듈에서 스프링 어플리케이션 실행
## 테스트 방법
- 통합 http 테스트는 /http/bingterpark.http에 있습니다.
- 어드민 플로우, 유저 플로우 http 코드를 위에서부터 하나씩 실행하시면 됩니다. 
