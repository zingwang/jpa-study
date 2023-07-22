

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
> SprngPhtysicaNamingStrategy 