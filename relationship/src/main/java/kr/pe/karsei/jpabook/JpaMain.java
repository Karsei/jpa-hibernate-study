package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.Member;
import kr.pe.karsei.jpabook.domain.scenario.MemberEager;
import kr.pe.karsei.jpabook.domain.scenario.MemberLazy;
import kr.pe.karsei.jpabook.domain.scenario.TeamEager;
import kr.pe.karsei.jpabook.domain.scenario.TeamLazy;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // persistence.xml 에 있는 이름
        // 어플리케이션에서 딱 하나만 생성되어야 하고, 전체에서 공유되어야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gazuua");
        // 고객 요청이 올 때마다 (DB 커넥션을 얻어서 뭔가 한다던가) 꼭 EntityManager 를 만들어야 한다.
        // 스레드간에 절대 공유가 되면 안 된다. (사용 후 버려야 함)
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 - 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 일대다 관련 시나리오 예시
            //checkManyToOne(em);
            //checkOneToMany(em);
            //checkOneToManyCaution(em);
            //checkOneToManyCaution2(em);
            //oneToMany(em);

            // 상속
            //inheritance(em);
            //inheritanceTablePerClassUnion(em);

            // 프록시
            //(emf, em);
            //proxyLazy(em);
            proxyEager(em);

            // 트랜잭션 - 종료
            tx.commit();
        }
        catch (Exception e) {
            // 트랜잭션 - 롤백
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }

        emf.close();
    }

    private static void proxy(EntityManagerFactory emf, EntityManager em) {
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

    private static void proxyLazy(EntityManager em) {
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

    private static void proxyEager(EntityManager em) {
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
