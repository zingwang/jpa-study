#### 스프링 데이터 JPA 정리

공통인터페이스 구성
> - 스프링데이터 <br>
> :Repository <- CrudRepository <- PagingAndSortingRepository <br>
> - 스프링데이터 JPA <br>
> :JpaRepository

"제네릭타입"
T: 엔티티, ID: 엔티티의 식별자타입, S: 엔티티와 그 자식의 타입

"주요메서드"
save(S), delete(T), findById(ID), getOne(ID), findAll(...)



쿼리 메소드기능
- 메소드 이름으로 쿼리생성
- 메소드 이름으로 JPA NamedQuery 호출
- @Query 어노테이션을 사용해서 리파지토리 인터페이스에 쿼리 직접 정의



- 컬렉션 파라미터 바인딩: in절 지원
- 반환타입이 유연


###### 스프링데이터 JPA 페이징과 정렬
###### 페이징과 정렬 파라미터
- org.springframework.data.domain.Sort : 정렬 기능
- org.springframework.data.domain.Pageable : 페이징 기능 (내부에 Sort 포함)
- CountQuery와 Query분리 가능(복잡한 로직인 경우 분리적용)

###### 특별한 반환 타입 (모바일에 주로 쓰이는)
- org.springframework.data.domain.Page : 추가 count 쿼리 결과를 포함하는 페이징
- org.springframework.data.domain.Slice : 추가 count 쿼리 없이 다음 페이지만 확인 가능(내부적으로 limit + 1조회)
List (자바 컬렉션): 추가 count 쿼리 없이 결과만 반환

###### 벌크성 수정 쿼리

벌크 연산 이후 조회를 할 경우: 영속성 컨텍스트를 clear하거나 clearAutomatically = true 설정으로 초기화 필요

###### 새로운 엔티티 구별
@GeneratedValue면 새로운 엔티티로 인식하나 @Id만 사용하면 merge()호출.
Persistable를 사용하여 새로운 엔티티 확인 여부를 직접 구현

######  Projections
- 엔티티 대신 DTO 편리하게 조회 할 때 사용
- 프로젝션 대상이 root 엔티티면 유용 (root 엔티티를 넘어가면 최적화X)
- 실무에서는 단순할 때 사용, 복잡해지면 QueryDSL 사용