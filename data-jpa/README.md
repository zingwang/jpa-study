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

###### 특별한 반환 타입 (모바일에 주로 쓰이는)
- org.springframework.data.domain.Page : 추가 count 쿼리 결과를 포함하는 페이징
- org.springframework.data.domain.Slice : 추가 count 쿼리 없이 다음 페이지만 확인 가능(내부적으로 limit + 1조회)
List (자바 컬렉션): 추가 count 쿼리 없이 결과만 반환