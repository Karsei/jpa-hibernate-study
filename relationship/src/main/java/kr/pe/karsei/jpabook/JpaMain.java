package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.scenario1.Member1;
import kr.pe.karsei.jpabook.domain.scenario1.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

            // 트랜잭션 - 종료
            tx.commit();
        }
        catch (Exception e) {
            // 트랜잭션 - 롤백
            tx.rollback();
        }
        finally {
            em.close();
        }

        emf.close();
    }

    private static void checkManyToOne(EntityManager em) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);

        Member1 member1 = new Member1();
        member1.setName("member1");
        member1.setTeam(team);
        em.persist(member1);

        em.flush();
        em.clear();

        Member1 findMember = em.find(Member1.class, member1.getId());
        Team findTeam = findMember.getTeam();
        System.out.println("findTeam = " + findTeam.getName());
    }

    private static void checkOneToMany(EntityManager em) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);

        Member1 member1 = new Member1();
        member1.setName("member1");
        member1.setTeam(team);
        em.persist(member1);

        em.flush();
        em.clear();

        Member1 findMember = em.find(Member1.class, member1.getId());
        List<Member1> members = findMember.getTeam().getMembers();
        for (Member1 member : members) {
            System.out.println("member = " + member.getName());
        }
    }

    private static void checkOneToManyCaution(EntityManager em) {
        Member1 member1 = new Member1();
        member1.setName("member1");
        em.persist(member1);

        Team team = new Team();
        team.setName("teamA");
        team.getMembers().add(member1);
        em.persist(team);

        em.flush();
        em.clear();
    }

    private static void checkOneToManyCaution2(EntityManager em) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);

        Member1 member1 = new Member1();
        member1.setName("member1");
        member1.changeTeam(team);
        em.persist(member1);

        //team.getMembers().add(member1); // Entity 에다가 추가

        //em.flush();
        //em.clear();

        Team findTeam = em.find(Team.class, team.getId()); // 1차 캐시
        List<Member1> members = findTeam.getMembers();
        
        System.out.println("================");
        for (Member1 m : members) {
            System.out.println("m = " + m.getName());
        }
        System.out.println("================");
    }

    private static void oneToMany(EntityManager em) {
        Member1 member = new Member1();
        member.setName("member1");
        em.persist(member);

        Team team = new Team();
        team.setName("teamA");
        team.getMembers().add(member);
        em.persist(team);
    }
}
