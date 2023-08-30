package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;



// 인터페이스 이름은 상관없지만 MemberRepositoryImpl Impl을 입력해줘야 함
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    
    private final EntityManager em;

    @Override
    public List findMEmberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
