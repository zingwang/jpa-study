

- TEST 설정
- 파일 > 설정(Ctrl+Alt+S)
- 빌드, 실행, 배포 > 빌드 도구 > gradle
- 다음을 사용하여 테스트 실행: Intellij IDEA

#섹션 2

엔티티클래스개발
- Getter,Setter 모두 제공하지말고 필요한 메서드
- 변경지점이 명확하도록 변경을 위한 "비즈니스 메서드를 별도로 제공"
- 실무에서는 @ManyToMany를 사용하지 말것 (운영상 이슈 발생)
- JPA 스펙상 엔티티나 임베디드 타입은 생성자를 public 또는 protected로 설정
  <br><br>
###### 엔티티설계시 주의점
-가급적 Setter를 사용하지 않기
> - 모든연관관계는 지연로딩으로 설정
> 즉시로딩은 예측과 실행추적이 어려워짐, "N+1 이슈발생"
> XToOne(OneToOne,ManyToOne) 관계는 기본이 즉시로딩(EAGER)이므로 지연로딩으로 설정
> - 컬렉션은 필드의 초기화하기
> - 테이블, 컬럼명 생성전략
> SpringPhtysicaNamingStrategy 

###### 변경 감지와 병합(merge)
준영속 엔티티 : 영속성 컨텍스트가 더는 관리하지 않는 엔티티
- 변경 감지 기능 (영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정)
- 병합 (식별자 값으로 영속 엔티티 조회 -> 준영속 엔티티의 값으로 모두 교체(병합))

* 병합은 모든 속성 변경으로 null로 업데이트 할 위험 (실무에서는 쓰지 않는 것이 좋음)

###### 쿼리 방식 선택 권장 순서
1. 우선 엔티티를 DTO로 변환하는 방법
2. 필요 시 페치 조인으로 성능을 최적화 
3. DTO로 직접 조회하는 방법
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용

###### 한계돌파
> 페이징+ 컬렉션 엔티티<br>
ToOne( OneToOne, ManyToOne) 관계 모두 페치조인 <br>
ToOne 관계는 row수를 증가시키지 않으므로 페이징 쿼리에 영향X<br>
컬레션은 지연로딩으로 조회<br>
지연 로딩 성능최적화에는 <br>
hibernate.default_batch_fetch_size, @BatchSize를 적용 > 설정size만큼 IN 쿼리조회<br>
hibernate.default_batch_fetch_size 글로벌 설정<br>
@BatchSize: 개별최적화 <br>

- 쿼리 호출 수 1+N -> 1 + 1 로 최적화, 페이징이 가능
- ToOne 관계는 페치 조인해도 페이징 영향 없으므로 쿼리수를 줄이고 나머지는 설정으로 최적화

- ToOne 관계들을 먼저조회하고 ToMany 관계는 각각 별도로 처리 (ToMany 관계는 조인하면 row수 증가하므로)

###### API 개발 고급 정리
- 엔티티조회, DTO 직접조회

권장순서 
1. 엔티티 조회 방식으로 우선 접근
   1. 페치 조인으로 쿼리수 최적화
   2. 컬렉션 최적화
      1. 페이징 필요 시 hibernate.default_batch_fetch_size, @BatchSize 로 최적화
      2. 페이징 필요X -> 페치조인
2. 엔티티 조회 방식으로 해결이 되지않으면 DTO 조회방식
3. DTO 조회 방식으로 해결이 안되면 'NativeSQL' or 스프링 'JdbcTemplate'

###### OSIV

- 커멘드와 쿼리 분리
- 실무에서 OSIV를 끈 상태로 복잡성을 관리하는 방법으로 Command와 Query를 분리 
exam
> OrderService
OrderService: 핵심 비즈니스 로직
OrderQueryService: 화면이나 API에 맞춘 서비스 (주로 읽기 전용 트랜잭션 사용)

* 고객 서비스의 실시간 API는 OSIV를 끄고, ADMIN 처럼 커넥션을 많이 사용하지 않는 곳에 서는 OSIV를 키는 것도 방법 
