package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.repository.MemberRepository;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamB);
        em.persist(teamA);

        Member memberA = new Member("memberA",10,teamA);
        Member memberB = new Member("memberB", 20, teamA);
        Member memberC = new Member("memberC", 30, teamB);
        Member memberD = new Member("memberD", 40, teamB);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberD);
        em.persist(memberC);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member="+member);
            System.out.println("member.team="+member.getTeam());
        }

    }


    @Test
    void JpaBaseEntityTest() throws InterruptedException {
        Member member1 = new Member("member1");
        memberRepository.save(member1);

        Thread.sleep(2000);
        member1.setUsername("member2");

        em.flush();
        em.clear();

        Optional<Member> findMember = memberRepository.findById(member1.getId());

        System.out.println("createDate="+findMember.get().getCreatedDate());
        System.out.println("updateDate="+findMember.get().getLastModifyDate());
        System.out.println("createdBy="+findMember.get().getCreatedBy());
        System.out.println("updatedBy="+findMember.get().getLastModifyBy());
    }

}