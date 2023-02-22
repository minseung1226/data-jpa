package study.datajpa.entity.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.awt.print.Pageable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Autowired
    EntityManager em;


    @Test
    void testMember(){
        Member member = new Member("userA",10,null);
        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        Assertions.assertThat(saveMember).isEqualTo(findMember).isEqualTo(member);

        System.out.println("member="+member);
        System.out.println("saveMember="+saveMember);

    }


    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long count2 = memberRepository.count();
        assertThat(count2).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 1);

        assertThat(members.get(0).getUsername()).isEqualTo("AAA");
        assertThat(members.get(0).getAge()).isEqualTo(10);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    void findByUsername(){
        Member userA = new Member("userA");
        memberRepository.save(userA);
        List<Member> findMember = memberRepository.findByUsername("userA");
        assertThat(userA).isEqualTo(findMember);
    }

    @Test
    void testQuery(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        List<Member> members = memberRepository.findUser("AAA", 1);

        assertThat(members.get(0).getUsername()).isEqualTo("AAA");
        assertThat(members.get(0).getAge()).isEqualTo(10);
        assertThat(members.size()).isEqualTo(1);
    }


    @Test
    void findUsernameList(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        List<String> members = memberRepository.findUsername();

        assertThat(members).contains(member1.getUsername()).contains(member2.getUsername());
    }

    @Test
    void findMemberDto(){
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("AAA", 10,team1);
        Member member2 = new Member("BBB", 20,team2);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("dto.id="+dto.getUsername());
            System.out.println("dto.username="+dto.getUsername());
            System.out.println("dto.teamname"+dto.getTeamName());
        }
    }

    @Test
    void findByNames(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

    }


    @Test
    void returnType(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findListByUsername("AAA");
        Member member = memberRepository.findMemberByUsername("AAA");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");
    }


    @Test
    void paging(){
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        int age=10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> members = page.getContent();
        long totalCount = page.getTotalElements();
        int totalPages = page.getTotalPages();

        for (Member member : members) {
            System.out.println("members="+member);
        }
        System.out.println("totalCount = " + totalCount);

        System.out.println("totalPages = " + totalPages);


    }

    @Test
    void slice(){
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        int age=10;
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);


        List<Member> members = page.getContent();

        for (Member member : members) {
            System.out.println("members="+member);
        }

    }


    @Test
    @Rollback(value = false)
    void bulkUpdate(){
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 19);
        Member member3 = new Member("member3", 20);
        Member member4 = new Member("member4", 21);
        Member member5 = new Member("member5", 40);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        System.out.println("===============");

        int resultCount = memberRepository.bulkAgePlus(20);
        System.out.println("===============");

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    void findMemberLazy(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityByUsername("member");

        for (Member member : members) {
            System.out.println("member="+member.getUsername());
            System.out.println("member.team="+member.getTeam());
        }
    }

    @Test
    void queryHint(){
        Member member1 = memberRepository.save(new Member("member1", 10));

        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    void lock(){
        Member member1 = memberRepository.save(new Member("member1", 10));

        em.flush();
        em.clear();

        List<Member> findMember = memberRepository.findLockByUsername(member1.getUsername());
        findMember.get(0).setUsername("member2");

        em.flush();

    }

    @Test
    void callCustom(){
        memberRepository.findMemberCustom();
    }

}