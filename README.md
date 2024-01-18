# Bingterpark

<img src="https://i.imgur.com/xRGQU4e.jpg" width="300">

<br/>

## 📌 프로젝트 목적

> 실제 서비스인 **인터파크 티켓**의 API를 클론코딩하여
> 데브코스에서 배웠던 내용을 적용해보고 협업을 경험해봅니다.

<br/>

## ⌛️ 프로젝트 기간

> 2023.12.15 ~ 2024.01.12 (4주)

<br/>

## 🎯도메인 별 목표 달성
>회원
>- Spring Security, OAuth를 이용한 회원가입,로그인,권한 관리 구현 및 시큐리티 내부 구조에 대한 이해
>
> 예매
>- Redis SortedSet 자료구조를 이용한 예매 대기열 구현
>- 토스페이먼츠 API를 이용한 결제 시스템 구현
>
>
>공연
>- 공연 별 좌석 관리 등 복잡한 연관관계 및 비즈니스 로직 설계
>- 검색 기능에 최적화 된 엘라스틱서치를 도입하여 키워드 검색 기능을 구현
>- 실시간 인기 검색어 Top 10 기능 구현

<br/>

## 👥 팀원

|           Product Owner            |            Scrum Master             |             Developer              |             Developer              |             Developer              |            Developer            |
|:----------------------------------:|:-----------------------------------:|:----------------------------------:|:----------------------------------:|:----------------------------------:|:-------------------------------:|
| [김경훈](https://github.com/KarmaPol) | [김대휘](https://github.com/marooo326) | [김별이](https://github.com/byulcode) | [박영재](https://github.com/park0jae) | [이한나](https://github.com/annahxxl) | [조은비](https://github.com/eunbc) |

<br/>



## 🛠️ 기술 스택

### 개발 환경

<p>
<img src="https://img.shields.io/badge/JAVA 17-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot 3-6DB33F?style=for-the-badge&logo=Spring boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security 6-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Batch-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
</p>

<p>
<img src="https://img.shields.io/badge/H2-004088?style=for-the-badge&logo=db&logoColor=white">
<img src="https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/QueryDsl-02A8EF?style=for-the-badge&logo=QueryDsl&logoColor=white">
<img src="https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">
</p>

<p>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
</p>

<p>
<img src="https://img.shields.io/badge/Logstash-005571?style=for-the-badge&logo=logstash&logoColor=white">
<img src="https://img.shields.io/badge/Kibana-005571?style=for-the-badge&logo=kibana&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">
</p>

### 협업 도구

<p>
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white">
<img src="https://img.shields.io/badge/Github-000000?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
</p>

<br/>

## 🚀협업 

### [팀 노션](https://www.notion.so/backend-devcourse/2-BingterPark-4ecfb3943d9c4a8f9bb83f72876b6a80)

### [Git Convention](https://github.com/Team-BingBong/BE-05-Bingterpark/wiki/Git-Convention)
<br/>

## ✨프로젝트 설계

### [유저스토리 & ERD](https://github.com/Team-BingBong/BE-05-Bingterpark/wiki/%EC%9C%A0%EC%A0%80%EC%8A%A4%ED%86%A0%EB%A6%AC-&-ERD)

### [모듈 구조](https://github.com/Team-BingBong/BE-05-Bingterpark/wiki/%EB%A9%80%ED%8B%B0%EB%AA%A8%EB%93%88-%EA%B5%AC%EC%A1%B0)

<br/>


## 실행 방법

1. git clone
2. RDB, 레디스 실행 ```docker-compose up -d```
3. api-event 모듈로 이동 ```cd /api/api-event```
4. 엘라스틱 서치 도커 이미지 빌드 ```docker build -t el:0.1 -f ./Dockerfile .```
5. ELK 스택 실행 ```docker-compose up -d```
6. api-booking, api-event, api-member 각 모듈에서 스프링 어플리케이션 실행

<br/>

## 테스트 방법

- 통합 http 테스트는 /http/bingterpark.http에 있습니다.
- 어드민 플로우, 유저 플로우 http 코드를 위에서부터 하나씩 실행하시면 됩니다.
