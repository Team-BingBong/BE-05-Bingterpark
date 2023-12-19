### core 모듈 규칙
- core 모듈 수정 시 팀원 모두에게 공지

### core 모듈 설명
#### core-domain
- CoreDomainConfig.class - 코어 모듈은 SpringBootApplication이 없으므로, 엔티티 스캔 설정
- 순수 도메인 관련 코드만 (Payment.class, EventRepository.class O) (EventService X)
#### core-infra
- mySql, Redis 등 인프라에 관련된 그래들 및 yml 파일, Config 설정
