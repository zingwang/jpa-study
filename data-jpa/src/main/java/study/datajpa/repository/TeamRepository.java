package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

//@Repository 가 없어도 스캔
public interface TeamRepository extends JpaRepository<Team, Long> {

}
