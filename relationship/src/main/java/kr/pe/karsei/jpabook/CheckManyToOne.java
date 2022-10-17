package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.scenario.Member1;
import kr.pe.karsei.jpabook.domain.scenario.Team;

import javax.persistence.EntityManager;

public class CheckManyToOne {
    public static void checkManyToOne(EntityManager em) {
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
}
