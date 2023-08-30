package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void testMember(){
        Member member= new Member("test A");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertEquals(member, findMember);
        assertEquals(member.getUsername(), findMember.getUsername());
    }

    
    //MemberJpaRepositoryTest와 유사코드
    @Test
    public void basicCRUD(){
        Member member1= new Member("member1");
        Member member2= new Member("member1");
        memberRepository.save(member1);
        memberRepository.save(member2);
        //단건 조회 검증
        Member findMember1 =  memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertEquals(findMember1,member2);
        assertEquals(findMember2,member2);
        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertEquals(all.size(),2);
        //카운트 검증
        long count = memberRepository.count();
        assertEquals(count,2);
        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertEquals(deletedCount,0);
    }
    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertEquals(result.get(0).getUsername(),"AAA");
        assertEquals(result.get(0).getAge(),20);
        assertEquals(result.size(),1);
    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);
        List<Member> result = memberRepository.findByUsername("AAA");
        assertEquals(result.get(0).getUsername(),"AAA");
        assertEquals(result.get(0),m1);
    }
    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findUser("AAA", 20);
        assertEquals(result.get(0).getUsername(),"AAA");
        assertEquals(result.get(0).getAge(),20);
        assertEquals(result.size(),1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<String> usernameList = memberRepository.findUsernameList();


        for (String s : usernameList) {
            System.out.println("s = "+s);
        }
    }

    @Test
    public void findMemberDto() {
        Team team= new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto s : memberDto) {
            System.out.println("s = "+s);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));


        for (Member m : result) {
            System.out.println("member = "+m);
        }
    }


    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));


        int age=10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age,pageRequest);
        Page<MemberDto> toMap = page.map( member-> new MemberDto(member.getId(),member.getUsername(),member.getTeam().getName()));

        //then
        List<Member> content = page.getContent();
        //long totalElements =  page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = "+member);
        }
        //System.out.println("totalElements = "+totalElements);
        //then

        assertEquals(content.size(),3);
        //assertEquals(totalElements,5);
        assertEquals(page.getNumber(),0); //page 번호
        //assertEquals(page.getTotalPages(),2); // page 개수
        assertEquals(page.isFirst(),true); // 처음페이지인지
        assertEquals(page.hasNext(),true); // 다음페이지있는지
    }

    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 13));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 25));
        memberRepository.save(new Member("member5", 35));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //entityManager.flush(); // 변경내용반영
        //entityManager.clear();


        List<Member> result=memberRepository.findByUsername("member5");

        Member member5 = result.get(0);
        System.out.println("member5 = "+member5); // 영속성을 날리지 않으면 36이아닌 35로출력되므로 clear나 clearAutomarically필요
        //then
        assertEquals(resultCount,3);
    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1= new Member("member1",10, teamA);
        Member member2= new Member("member2",10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        //when
        //select Member
        List<Member> members1 = memberRepository.findAll();

        for(Member member: members1){
            System.out.println("member1: "+ member.getUsername());
            System.out.println("member1.teamClass: "+ member.getTeam().getClass());
            System.out.println("member1.team: "+ member.getTeam().getName());
        }

        //select Member
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for(Member member: members){
            System.out.println("member: "+ member.getUsername());
            System.out.println("member.teamClass: "+ member.getTeam().getClass());
            System.out.println("member.team: "+ member.getTeam().getName());
        }

    }
    @Test
    public void queryHint(){
        //given
        Member member1= new Member("member1", 10);
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        //when

        //QueryHint 로 변경감지 체크X
        Member findMember=memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        entityManager.flush();

    }
    @Test
    public void lock(){
        //given
        Member member1= new Member("member1", 10);
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        //when

        //QueryHint 로 변경감지 체크X
        List<Member> member=memberRepository.findLockByUsername("member1");


        entityManager.flush();

    }

    @Test
    public void callCustom(){
        List<Member> result= memberRepository.findMEmberCustom();
    }

}