package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.Member;
import kr.pe.karsei.jpabook.domain.scenario.MemberEager;
import kr.pe.karsei.jpabook.domain.scenario.MemberLazy;
import kr.pe.karsei.jpabook.domain.scenario.TeamEager;
import kr.pe.karsei.jpabook.domain.scenario.TeamLazy;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CheckProxy {
    public static void proxy(EntityManagerFactory emf, EntityManager em) {
        Member member = new Member();
        member.setName("hi");
        em.persist(member);

        em.flush();
        em.clear();

        Member ref = em.getReference(Member.class, member.getId());
        System.out.println("ref = " + ref.getClass()); // Proxy

        // 프록시의 초기화를 안했기 때문에 false
        System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(ref));
        ref.getName(); // SELECT 실행
        // 프록시의 초기화를 했기 때문에 true
        System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(ref));
        // 강제 초기화 (사실, JPA 표준에는 강제 초기화가 없다)
        Hibernate.initialize(ref);
    }

    public static void proxyLazy(EntityManager em) {
        TeamLazy team = new TeamLazy();
        team.setName("단풍나무");
        em.persist(team);

        MemberLazy member = new MemberLazy();
        member.setName("Mr. Hong");
        member.setTeam(team);
        em.persist(member);

        em.flush();
        em.clear();

        MemberLazy findMember = em.find(MemberLazy.class, member.getId());
        System.out.println("findMember = " + findMember.getTeam().getClass());

        System.out.println("=============");
        findMember.getTeam().getName();
        System.out.println("=============");
    }

    public static void proxyEager(EntityManager em) {
        TeamEager team = new TeamEager();
        team.setName("단풍나무");
        em.persist(team);

        TeamEager team2 = new TeamEager();
        team2.setName("소나무");
        em.persist(team2);

        MemberEager member = new MemberEager();
        member.setName("Mr. Hong");
        member.setTeam(team);
        em.persist(member);

        MemberEager member2 = new MemberEager();
        member2.setName("Mr. Back");
        member2.setTeam(team2);
        em.persist(member2);

        em.flush();
        em.clear();

        // SQL : SELECT * FROM MEMBER
        // SQL : SELECT * FROM TEAM WHERE TEAM_ID = xxx
        List<MemberEager> list = em.createQuery("select m from MemberEager m", MemberEager.class).getResultList();
    }
}
