package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.scenario.Member1;
import kr.pe.karsei.jpabook.domain.scenario.Team;

import javax.persistence.EntityManager;
import java.util.List;

public class CheckOneToMany {
    public static void checkOneToMany(EntityManager em) {
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

    public static void checkOneToManyCaution(EntityManager em) {
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

    public static void checkOneToManyCaution2(EntityManager em) {
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

    public static void oneToMany(EntityManager em) {
        Member1 member = new Member1();
        member.setName("member1");
        em.persist(member);

        Team team = new Team();
        team.setName("teamA");
        team.getMembers().add(member);
        em.persist(team);
    }
}
