package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){

        System.out.println("memberJpaRepository = " + memberJpaRepository.getClass());
        Member member= new Member("test A");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertEquals(member, findMember);
        assertEquals(member.getUsername(), findMember.getUsername());
    }


    @Test
    public void basicCRUD(){
        Member member1= new Member("member1");
        Member member2= new Member("member1");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        //단건 조회 검증
        Member findMember1 =  memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertEquals(findMember1,member2);
        assertEquals(findMember2,member2);
        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertEquals(all.size(),2);
        //카운트 검증
        long count = memberJpaRepository.count();
        assertEquals(count,2);
        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        assertEquals(deletedCount,0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertEquals(result.get(0).getUsername(),"AAA");
        assertEquals(result.get(0).getAge(),20);
        assertEquals(result.size(),1);
    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA", 10);
        memberJpaRepository.save(m1);
        List<Member> result = memberJpaRepository.findByUsername("AAA");
        assertEquals(result.get(0).getUsername(),"AAA");
        assertEquals(result.get(0),m1);
    }
}